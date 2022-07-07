package com.hetongxue.security.filter;

import com.hetongxue.lang.Const;
import com.hetongxue.security.exception.CaptchaException;
import com.hetongxue.security.handler.LoginFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 验证码过滤器
 * @ClassNmae: CaptchaFilter
 * @Author: 何同学
 * @DateTime: 2022-07-07 22:18
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 当不是登录页时的处理
            if (!request.getRequestURI().equals("/login")) {
                filterChain.doFilter(request, response);
                return;
            }
            // 当是登录页时的处理
            if (request.getRequestURI().equals("/login") && request.getMethod().equalsIgnoreCase("POST")) {
                String code = request.getParameter("code");
                String sessionCode = (String) request.getSession().getAttribute(Const.CAPTCHA_KEY);
                if (code == null || code.equals("")) throw new CaptchaException("验证码不能为空！");
                if (sessionCode == null || sessionCode.equals("")) throw new CaptchaException("验证码过期");
                if (!code.equals(sessionCode)) throw new CaptchaException("验证码错误");
                request.getSession().removeAttribute(Const.CAPTCHA_KEY);
                filterChain.doFilter(request, response);
            }
        } catch (CaptchaException e) {
            request.getSession().removeAttribute(Const.CAPTCHA_KEY);
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
    }

}