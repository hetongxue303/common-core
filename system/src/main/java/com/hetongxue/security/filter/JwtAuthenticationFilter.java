package com.hetongxue.security.filter;

import com.hetongxue.lang.Const;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.service.UserService;
import com.hetongxue.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(Const.AUTHORIZATION_KEY);
        // token不存在时的处理
        if (token == null || token.equals("")) {
            chain.doFilter(request, response);
            return;
        }
        // token存在时的处理
        Claims claims = jwtUtils.getClaims(token);
        if (claims == null) {
            throw new JwtException("token 不存在");
        }
        if (jwtUtils.isExpired(claims)) {
            throw new JwtException("token 已过期");
        }
        String username = claims.getSubject();
        User user = (User) userService.loadUserByUsername(username);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities()));
        chain.doFilter(request, response);
    }

}