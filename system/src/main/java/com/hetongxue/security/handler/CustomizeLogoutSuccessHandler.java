package com.hetongxue.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetongxue.response.Result;
import com.hetongxue.security.lang.Const;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 用户注销成功处理类
 * @ClassNmae: CustomizeLogoutSuccessHandler
 * @Author: 何同学
 * @DateTime: 2022-07-07 22:14
 */
@Component
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        redisTemplate.delete(Const.AUTHORIZATION_KEY);
        response.getWriter().println(new ObjectMapper().writeValueAsString(Result.Success().setMessage("注销成功")));
    }

}