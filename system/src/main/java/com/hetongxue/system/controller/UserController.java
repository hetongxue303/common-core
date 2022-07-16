package com.hetongxue.system.controller;

import com.hetongxue.aop.log.LogAnnotation;
import com.hetongxue.response.Result;
import com.hetongxue.system.domain.vo.UserQueryVo;
import com.hetongxue.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户模块
 * @ClassNmae: UserController
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:54
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    @LogAnnotation(module = "用户列表", operate = "分页获取用户列表")
    public Result getUserList(UserQueryVo userQuery) {
        return Result.Success().setMessage("查询成功").setData(userService.selectUserList(userQuery));
    }

}