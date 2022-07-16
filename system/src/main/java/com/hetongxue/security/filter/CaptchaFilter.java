package com.hetongxue.security.filter;

import com.hetongxue.lang.Const;
import com.hetongxue.response.ResponseCode;
import com.hetongxue.security.exception.CaptchaAuthenticationException;
import com.hetongxue.security.handler.LoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

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
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {

    private final LoginFailureHandler loginFailureHandler;

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String LOGIN_METHOD = "POST";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 当不是登录页时的处理
            if (!request.getRequestURI().equals(Const.LOGIN_PATH)) {
                doFilter(request, response, filterChain);
                return;
            }
            // 当是登录页时的处理
            if (request.getRequestURI().equals(Const.LOGIN_PATH) && request.getMethod().equalsIgnoreCase(LOGIN_METHOD)) {
                String code = request.getParameter(Const.VERIFICATION_CODE);
                String redisCode = (String) redisTemplate.opsForValue().get(Const.CAPTCHA_KEY);
                // 验证码为空
                if (ObjectUtils.isEmpty(code)) {
                    throw new CaptchaAuthenticationException(ResponseCode.VALIDATION_NULL.getCode().toString());
                }
                // 验证码过期
                if (ObjectUtils.isEmpty(redisCode)) {
                    throw new CaptchaAuthenticationException(ResponseCode.VALIDATION_EXPIRED.getCode().toString());
                }
                // 验证码错误
                if (!code.equals(redisCode)) {
                    throw new CaptchaAuthenticationException(ResponseCode.VALIDATION_ERROR.getCode().toString());
                }
                redisTemplate.delete(Const.CAPTCHA_KEY);
                doFilter(request, response, filterChain);
            }
        } catch (CaptchaAuthenticationException e) {
            redisTemplate.delete(Const.CAPTCHA_KEY);
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
    }

}