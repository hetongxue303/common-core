package com.hetongxue.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Description: 自定义JWT异常类
 * @ClassNmae: JwtAuthenticationException
 * @Author: 何同学
 * @DateTime: 2022-07-19 21:51
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

}