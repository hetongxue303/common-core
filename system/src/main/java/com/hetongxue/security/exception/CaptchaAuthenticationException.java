package com.hetongxue.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Description: 验证码异常类
 * @ClassNmae: CaptchaAuthenticationException
 * @Author: 何同学
 * @DateTime: 2022-07-07 22:12
 */
public class CaptchaAuthenticationException extends AuthenticationException {

    public CaptchaAuthenticationException(String message) {
        super(message);
    }

}