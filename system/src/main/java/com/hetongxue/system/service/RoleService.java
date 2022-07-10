package com.hetongxue.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hetongxue.system.domain.Role;

import java.util.List;

/**
 * @Description: 角色业务
 * @InterfaceNmae: RoleService
 * @Author: 何同学
 * @DateTime: 2022-07-07 14:19
 */
public interface RoleService extends IService<Role> {

    /**
     * 按用户ID加载用户角色信息
     */
    List<Role> loadRoleByUserId(Long userId);

}