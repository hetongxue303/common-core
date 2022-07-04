package com.hetongxue.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hetongxue.system.domain.User;

import java.util.List;

/**
 * @Description: 用户业务
 * @InterfaceNmae: UserService
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:47
 */
public interface UserService extends IService<User> {
    /**
     * 获取所有用户信息
     */
    List<User> getUserAll();

}