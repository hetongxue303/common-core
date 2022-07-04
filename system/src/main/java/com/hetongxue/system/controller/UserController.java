package com.hetongxue.system.controller;

import com.hetongxue.response.Result;
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

    @GetMapping("/getUserAll")
    public Result getUserAll() {
        return Result.Success(userService.getUserAll());
    }

}