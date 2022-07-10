package com.hetongxue.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hetongxue.security.SecurityUtils;
import com.hetongxue.system.domain.Permission;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.mapper.UserMapper;
import com.hetongxue.system.service.PermissionService;
import com.hetongxue.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Permission> permissions = permissionService.loadPermissionByUserId(user.getId());
        return user.setPermissions(SecurityUtils.generatePermission(permissions, 0L))
                .setAuthorities(SecurityUtils.generateAuthority(permissions));
    }


}