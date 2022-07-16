package com.hetongxue.security.filter;

import com.hetongxue.lang.Const;
import com.hetongxue.security.exception.JwtAuthenticationException;
import com.hetongxue.security.handler.LoginFailureHandler;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.service.UserService;
import com.hetongxue.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: JWT认证过滤器(实现自动登录)
 * @ClassNmae: JwtAuthenticationFilter
 * @Author: 何同学
 * @DateTime: 2022-07-07 22:19
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getRequestURI().equals("/getVerify")) {
                doFilter(request, response, filterChain);
                return;
            }
            // 判断是否为登录请求
            if (!request.getRequestURI().equals(Const.LOGIN_PATH)) {
                // 获取请求头中的token
                String token = request.getHeader(Const.AUTHORIZATION_KEY);
                // 判断token是否存在
                if (ObjectUtils.isEmpty(token)) {
                    throw new JwtAuthenticationException("token 不存在");
                }
                // 获取redis中的token
                String redisToken = (String) redisTemplate.opsForValue().get(Const.AUTHORIZATION_KEY);
                // 判断redis中token是否存在
                if (ObjectUtils.isEmpty(redisToken)) {
                    throw new JwtAuthenticationException("token 验证失败");
                }
                // 判断redis中的与header中的token是否一致
                if (!token.equals(redisToken)) {
                    throw new JwtAuthenticationException("token 不一致");
                }
                // 获取token的Claims
                Claims claims = jwtUtils.getClaims(token);
                String username = String.valueOf(claims.get("username"));
                // 解析token信息
                if (ObjectUtils.isEmpty(claims) || ObjectUtils.isEmpty(username)) {
                    throw new JwtAuthenticationException("token 解析失败");
                }
                // 判断token是否过期
                if (jwtUtils.isExpired(claims)) {
                    throw new JwtAuthenticationException("token 已过期");
                }
                // 获取用户信息
                User user = (User) userService.loadUserByUsername(username);
                // 判断用户信息是否为空
                if (ObjectUtils.isEmpty(user)) {
                    throw new JwtAuthenticationException("token 验证失败");
                }
                // 创建用户身份认证对象
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                // 设置请求信息
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 交给springSecurity上下文管理
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            doFilter(request, response, filterChain);
        } catch (AuthenticationException e) {
            // 发生错误时走认证失败处理器
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
    }

}