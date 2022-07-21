package com.hetongxue.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetongxue.response.ResponseCode;
import com.hetongxue.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 自定义会话过期处理类
 * @ClassNmae: CustomizeExpiredSessionStrategy
 * @Author: 何同学
 * @DateTime: 2022-07-21 23:41
 */
@Component
public class CustomizeExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        HttpServletResponse response = event.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().println(new ObjectMapper().writeValueAsString(Result.Error().setCode(ResponseCode.BAD_REQUEST.getCode()).setMessage("会话过期！")));
    }

}