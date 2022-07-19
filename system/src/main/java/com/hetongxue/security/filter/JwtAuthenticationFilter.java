package com.hetongxue.security.filter;

import com.hetongxue.security.exception.JwtAuthenticationException;
import com.hetongxue.security.lang.Const;
import com.hetongxue.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description: jwt过滤器(自动登录)
 * @ClassNmae: JwtAuthenticationFilter
 * @Author: 何同学
 * @DateTime: 2022-07-19 20:17
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取token
        String token = request.getHeader(Const.AUTHORIZATION_KEY);
        // 判断是否存在
        if (ObjectUtils.isEmpty(token)) {
            chain.doFilter(request, response);
            return;
        }
        // 判断token是否合法
        Claims claims = jwtUtils.getClaims(token);
        if (ObjectUtils.isEmpty(claims)) {
            throw new JwtAuthenticationException("token 异常");
        }
        // 判断token是否过期
        String redisToken = String.valueOf(redisTemplate.opsForValue().get(Const.AUTHORIZATION_KEY));
        if (jwtUtils.isExpired(claims) || ObjectUtils.isEmpty(redisToken)) {
            throw new JwtAuthenticationException("token 过期");
        }
        // 判断token与redis中的是否一致
        if (!Objects.equals(token, redisToken)) {
            throw new JwtAuthenticationException("token 不一致");
        }
        // 通过token拿到当前用户名
        String username = String.valueOf(claims.get("username"));
        // 获取用户权限信息并设置到上下文(用户名，密码，权限信息)
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, null));
        // 过滤器继续往后走
        chain.doFilter(request, response);
    }

}