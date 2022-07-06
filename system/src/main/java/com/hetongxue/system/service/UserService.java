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

    /**
     * 通过用户名获取用户信息
     */
    User getUserByUsername(String username);

    /**
     * 通过用户ID获取用户信息
     */
    User getUserByUid(Long uid);

    /**
     * 通过用户ID修改用户密码
     */
    int updatePasswordByUid(User user);

    /**
     * 通过用户ID修改用户信息
     */
    int updateUserByUid(User user);

    /**
     * 通过用户ID删除用户信息
     */
    int deleteUserByUid(Long uid);

    /**
     * 新增用户信息
     */
    int insertUser(User user);

}