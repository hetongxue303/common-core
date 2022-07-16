package com.hetongxue.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hetongxue.system.domain.Permission;

import java.util.List;

/**
 * @Description: 权限业务
 * @InterfaceNmae: PermissionService
 * @Author: 何同学
 * @DateTime: 2022-07-07 14:21
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 按用户ID加载用户权限列表
     */
    List<Permission> selectPermissionByUserId(Long userId);

}