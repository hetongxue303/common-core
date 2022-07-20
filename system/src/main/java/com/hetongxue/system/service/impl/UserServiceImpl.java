package com.hetongxue.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hetongxue.security.utils.SecurityUtils;
import com.hetongxue.system.domain.Permission;
import com.hetongxue.system.domain.Role;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.domain.vo.UserQueryVo;
import com.hetongxue.system.domain.vo.permission.MenuVo;
import com.hetongxue.system.domain.vo.permission.RouterVo;
import com.hetongxue.system.mapper.UserMapper;
import com.hetongxue.system.service.PermissionService;
import com.hetongxue.system.service.RoleService;
import com.hetongxue.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 用户业务实现
 * @ClassNmae: UserServiceImpl
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:47
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 获取权限 角色列表
        List<Permission> permissions = permissionService.selectPermissionByUserId(user.getId());
        List<Role> roles = roleService.selectRoleByUserId(user.getId());

        // 生成权限列表
        String authoritySaveName = "code";
        String authority = "";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(authoritySaveName))) {
            authority = (String) redisTemplate.opsForValue().get(authoritySaveName);
        } else {
            authority = SecurityUtils.generateAuthority(roles, permissions);
            redisTemplate.opsForValue().set(authoritySaveName, authority, 30, TimeUnit.MINUTES);
        }

        // 生成路由树
        String routerSaveName = "routers";
        List<RouterVo> routers = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(routerSaveName))) {
            routers = (List<RouterVo>) redisTemplate.opsForValue().get(routerSaveName);
        } else {
            routers = SecurityUtils.generateRouter(permissions, 0L);
            redisTemplate.opsForValue().set(routerSaveName, routers, 30, TimeUnit.MINUTES);
        }

        // 生成菜单树
        String menuSaveName = "menus";
        List<MenuVo> menus = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(menuSaveName))) {
            menus = (List<MenuVo>) redisTemplate.opsForValue().get(menuSaveName);
        } else {
            menus = SecurityUtils.generateMenu(permissions, 0L);
            redisTemplate.opsForValue().set(menuSaveName, routers, 30, TimeUnit.MINUTES);
        }

        return user.setMenus(menus)
                .setRouters(routers)
                .setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(authority));
    }

    @Override
    public IPage<User> selectUserList(UserQueryVo userQuery) {
        return userMapper.selectPage(new Page<>(userQuery.getPage(), userQuery.getSize()), new QueryWrapper<User>()
                .like(!ObjectUtils.isEmpty(userQuery.getUsername()), "username", userQuery.getUsername())
                .like(!ObjectUtils.isEmpty(userQuery.getNickName()), "nick_name", userQuery.getNickName())
                .like(!ObjectUtils.isEmpty(userQuery.getRealName()), "real_name", userQuery.getRealName())
                .like(!ObjectUtils.isEmpty(userQuery.getGender()), "gender", userQuery.getGender())
        );
    }
}