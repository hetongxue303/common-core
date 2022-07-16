package com.hetongxue.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hetongxue.system.domain.Permission;
import com.hetongxue.system.mapper.PermissionMapper;
import com.hetongxue.system.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 权限业务实现
 * @ClassNmae: PermissionServiceImpl
 * @Author: 何同学
 * @DateTime: 2022-07-07 14:21
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Permission> selectPermissionByUserId(Long userId) {
        return permissionMapper.selectList(new QueryWrapper<Permission>()
                .inSql("id", "select permission_id from sys_role_permission where role_id in " +
                        "(select distinct role_id from sys_user_role where user_id = " + userId + ")")
                .orderByAsc("menu_sort"));
    }
}