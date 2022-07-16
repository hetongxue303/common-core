package com.hetongxue.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.domain.vo.UserQueryVo;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Description: 用户业务
 * @InterfaceNmae: UserService
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:47
 */
public interface UserService extends IService<User>, UserDetailsService {

    /**
     * 查询用户列表
     */
    IPage<User> selectUserList(UserQueryVo userQuery);

}