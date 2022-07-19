package com.hetongxue.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetongxue.response.ResponseCode;
import com.hetongxue.response.Result;
import com.hetongxue.security.exception.CaptchaAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 认证失败处理器
 * @ClassNmae: LoginFailureHandler
 * @Author: 何同学
 * @DateTime: 2022-07-07 22:05
 */
@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Result result = Result.Error().setMessage("登陆失败，未知异常！");
        response.setContentType("application/json;charset=utf-8");
        if (exception instanceof AccountExpiredException) {
            result.setMessage("账户已过期");
        }
        if (exception instanceof BadCredentialsException) {
            result.setMessage("用户名或密码错误");
        }
        if (exception instanceof CredentialsExpiredException) {
            result.setMessage("密码已过期");
        }
        if (exception instanceof DisabledException) {
            result.setMessage("账户被禁用");
        }
        if (exception instanceof LockedException) {
            result.setMessage("账户已被锁");
        }
        if (exception instanceof InternalAuthenticationServiceException) {
            result.setMessage("账户不存在");
        }
        // 认证服务异常
        if (exception instanceof AuthenticationServiceException) {
            result.setMessage(exception.getMessage());
        }
        // 用户不存在
        if (exception instanceof UsernameNotFoundException) {
            result.setMessage(exception.getMessage());
        }
        // 验证码异常
        if (exception instanceof CaptchaAuthenticationException) {
            switch (Integer.parseInt(exception.getMessage())) {
                case 5000:
                    result.setCode(ResponseCode.VALIDATION_ERROR.getCode()).setMessage(ResponseCode.VALIDATION_ERROR.getMessage());
                    break;
                case 5001:
                    result.setCode(ResponseCode.VALIDATION_EXPIRED.getCode()).setMessage(ResponseCode.VALIDATION_EXPIRED.getMessage());
                    break;
                case 5002:
                    result.setCode(ResponseCode.VALIDATION_NULL.getCode()).setMessage(ResponseCode.VALIDATION_NULL.getMessage());
                    break;
            }
        }
        response.getWriter().println(new ObjectMapper().writeValueAsString(result));
    }

}