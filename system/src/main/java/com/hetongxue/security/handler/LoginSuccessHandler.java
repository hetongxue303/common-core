package com.hetongxue.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetongxue.lang.Const;
import com.hetongxue.response.Result;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.service.PermissionService;
import com.hetongxue.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 认证成功处理器
 * @ClassNmae: LoginSuccessHandler
 * @Author: 何同学
 * @DateTime: 2022-07-07 22:02
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private PermissionService permissionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 设置字符编码
        response.setContentType("application/json;charset=utf-8");
        // 设置状态
        response.setStatus(HttpStatus.OK.value());
        // 生成token
        response.setHeader(Const.AUTHORIZATION_KEY, jwtUtils.generateToken(authentication.getName()));
        // 自定义返回内容
        User user = (User) authentication.getPrincipal();
        user.setPassword(null);
        response.getWriter().println(new ObjectMapper()
                .writeValueAsString(Result.Success()
                        .setMessage("登陆成功")
                        .setData(user)
                ));
    }

}