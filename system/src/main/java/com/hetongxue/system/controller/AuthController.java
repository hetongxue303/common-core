package com.hetongxue.system.controller;

import com.hetongxue.lang.Const;
import com.hetongxue.response.Result;
import com.hetongxue.utils.HttpUtil;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 安全模块
 * @ClassNmae: AuthController
 * @Author: 何同学
 * @DateTime: 2022-07-07 21:16
 */
@RestController
public class AuthController {

    /**
     * 获取验证码
     */
    @GetMapping("/getVerify")
    public Result getVerify() {
        // 在java11中使用Nashorn engine  会出现 Warning: Nashorn engine is planned to be removed from a future JDK release
        System.setProperty("nashorn.args", "--no-deprecation-warning");// 解决上诉问题设置
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36, 2);
        // 存储于redis中 这里测试先放在session
        HttpUtil.getSession().setAttribute(Const.CAPTCHA_KEY, captcha.text());
        return Result.Success(captcha.toBase64()).setMessage(captcha.text());
    }

}