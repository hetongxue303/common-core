package com.hetongxue.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Description: 自定义JWT异常
 * @ClassNmae: JwtAuthenticationException
 * @Author: 何同学
 * @DateTime: 2022-07-16 18:54
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

}