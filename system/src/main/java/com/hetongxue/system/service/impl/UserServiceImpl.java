package com.hetongxue.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hetongxue.security.SecurityUtils;
import com.hetongxue.system.domain.Permission;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.domain.vo.UserQueryVo;
import com.hetongxue.system.mapper.UserMapper;
import com.hetongxue.system.service.PermissionService;
import com.hetongxue.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

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

    private final UserMapper userMapper;
    private final PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 获取用户对应的权限列表
        List<Permission> permissions = permissionService.selectPermissionByUserId(user.getId());
        return user.setRouters(SecurityUtils.generateRouter(permissions, 0L))
                .setMenus(SecurityUtils.generateMenu(permissions, 0L))
                .setAuthorities(SecurityUtils.generateAuthority(permissions));
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