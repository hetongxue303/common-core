package com.hetongxue.security.filter;

import com.hetongxue.lang.Const;
import com.hetongxue.response.ResponseCode;
import com.hetongxue.security.exception.CaptchaException;
import com.hetongxue.security.handler.LoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 当不是登录页时的处理
            if (!request.getRequestURI().equals(Const.LOGIN_PATH)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 当是登录页时的处理
            if (request.getRequestURI().equals(Const.LOGIN_PATH) && request.getMethod().equalsIgnoreCase("POST")) {
                String code = request.getParameter(Const.VERIFICATION_CODE);
//                String sessionCode = (String) request.getSession().getAttribute(Const.CAPTCHA_KEY);
                String redisCode = (String) redisTemplate.opsForValue().get(Const.CAPTCHA_KEY);
                // 验证码为空
                if (code == null || code.equals(""))
                    throw new CaptchaException(ResponseCode.VALIDATION_NULL.getCode().toString());
                // 验证码过期
                if (redisCode == null || redisCode.equals(""))
                    throw new CaptchaException(ResponseCode.VALIDATION_EXPIRED.getCode().toString());
                // 验证码错误
                if (!code.equals(redisCode))
                    throw new CaptchaException(ResponseCode.VALIDATION_ERROR.getCode().toString());
//                request.getSession().removeAttribute(Const.CAPTCHA_KEY);
                redisTemplate.delete(Const.CAPTCHA_KEY);
                filterChain.doFilter(request, response);
            }
        } catch (CaptchaException e) {
//            request.getSession().removeAttribute(Const.CAPTCHA_KEY);
            redisTemplate.delete(Const.CAPTCHA_KEY);
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
    }

}