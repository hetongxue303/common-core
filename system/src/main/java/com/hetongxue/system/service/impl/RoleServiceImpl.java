package com.hetongxue.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hetongxue.system.domain.Role;
import com.hetongxue.system.mapper.RoleMapper;
import com.hetongxue.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 角色业务实现
 * @ClassNmae: RoleServiceImpl
 * @Author: 何同学
 * @DateTime: 2022-07-07 14:19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Role> loadRoleByUserId(Long userId) {
        return roleMapper.selectList(new QueryWrapper<Role>()
                .inSql("id", "select distinct role_id from sys_user_role where user_id = " + userId));
    }
}