package com.hetongxue.system.controller;

import com.hetongxue.lang.Const;
import com.hetongxue.response.Result;
import com.hetongxue.system.domain.User;
import com.hetongxue.utils.JwtUtils;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 安全模块
 * @ClassNmae: AuthController
 * @Author: 何同学
 * @DateTime: 2022-07-07 21:16
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取验证码
     */
    @GetMapping("/getVerify")
    public Result getVerify() {
        // 在java11中使用Nashorn engine  会出现 Warning: Nashorn engine is planned to be removed from a future JDK release
        System.setProperty("nashorn.args", "--no-deprecation-warning");// 解决上诉问题设置
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36, 2);
        redisTemplate.opsForValue().set(Const.CAPTCHA_KEY, captcha.text(), 60, TimeUnit.SECONDS);// 设置60秒过期
        return Result.Success(captcha.toBase64());
    }

    /**
     * 刷新token
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        // 获取token信息
        String token = request.getHeader(Const.AUTHORIZATION_KEY);
        // 判断是否存在
        if (ObjectUtils.isEmpty(token) || ObjectUtils.isEmpty(jwtUtils.getClaims(token))) {
            return Result.Error().setMessage("token刷新失败");
        }
        // 从SpringSecurity上下文中获取信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 获取用户身份信息
        User user = (User) authentication.getPrincipal();
        // 生成新的token
        String newToken = jwtUtils.generateToken(user.getId(), user.getUsername());
        redisTemplate.opsForValue().set(Const.AUTHORIZATION_KEY, newToken, 7, TimeUnit.DAYS);
        return Result.Success().setData("token刷新成功");
    }

}