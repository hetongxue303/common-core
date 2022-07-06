package com.hetongxue.system.controller;

import com.hetongxue.aop.log.LogAnnotation;
import com.hetongxue.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 测试模块
 * @ClassNmae: TestController
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:12
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test")
    @LogAnnotation(module = "测试", operate = "测试项目是否可用")
    public Result test(@RequestParam String name) {
        return Result.Success().setMessage(name + ",欢迎使用,通用后台管理系统!");
    }

}