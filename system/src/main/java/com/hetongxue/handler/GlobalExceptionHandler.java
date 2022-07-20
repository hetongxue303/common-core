package com.hetongxue.handler;

import com.hetongxue.response.ResponseCode;
import com.hetongxue.response.Result;
import com.hetongxue.security.exception.JwtAuthenticationException;
import com.hetongxue.security.handler.CustomizeAccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 全局异常处理
 * @ClassNmae: GlobalExceptionHandler
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:07
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        response.setStatus(ResponseCode.INSUFFICIENT_STORAGE.getCode());
        return Result.Error()
                .setMessage(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                .setCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeException(RuntimeException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        response.setStatus(ResponseCode.INSUFFICIENT_STORAGE.getCode());
        return Result.Error()
                .setMessage(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                .setCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result nullPointerException(NullPointerException e) {
        e.printStackTrace();
        log.error(ResponseCode.NULL_POINTER.getMessage());
        return Result.Error()
                .setMessage(ResponseCode.NULL_POINTER.getMessage())
                .setCode(ResponseCode.NULL_POINTER.getCode());
    }

    /**
     * 权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void accessDeniedException(AccessDeniedException e) throws ServletException, IOException {
        CustomizeAccessDeniedHandler accessDeniedHandler = new CustomizeAccessDeniedHandler();
        accessDeniedHandler.handle(request, response, e);
    }

    /**
     * JWT异常
     */
    @ExceptionHandler(JwtAuthenticationException.class)
    public Result jwtAuthenticationFilter(JwtAuthenticationException e) {
        log.error(e.getMessage());
        return Result.Error().setMessage(e.getMessage());
    }

}