package com.hetongxue.configuration.security;

import com.hetongxue.security.filter.CaptchaFilter;
import com.hetongxue.security.filter.JwtAuthenticationFilter;
import com.hetongxue.security.handler.*;
import com.hetongxue.security.lang.Const;
import com.hetongxue.system.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * @Description: SpringSecurity配置类
 * @ClassNmae: SpringSecurityConfiguration
 * @Author: 何同学
 * @DateTime: 2022-07-07 22:16
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String[] REQUEST_WHITE_LIST = {"/login", "/getVerify"};
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomizeLogoutSuccessHandler logoutSuccessHandler;
    private final CustomizeAccessDeniedHandler accessDeniedHandler;
    private final CustomizeAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomizeExpiredSessionStrategy expiredSessionStrategy;
    private final UserServiceImpl userDetailsService;
    private final CaptchaFilter captchaFilter;

    /**
     * 密码加密处理
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * jwt异常处理
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    /**
     * 会话实例记录(新版本可以不用配置 但建议还是加上避免不必要的错误发生)
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 登陆认证处理
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 放行白名单请求 其余请求全部拦截
        http.authorizeRequests().antMatchers(REQUEST_WHITE_LIST).permitAll().anyRequest().authenticated();

        // 设置表单登录 设置登录请求地址
        http.formLogin().loginProcessingUrl(Const.LOGIN_PATH)
                // 设置登陆参数
                .usernameParameter(Const.USERNAME).passwordParameter(Const.PASSWORD)
                // 设置认证成功处理器 和 认证失败处理器
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                // 开启注销登录 注销请求地址 设置注销成功处理类
                .and().logout().logoutUrl(Const.LOGOUT_PATH).logoutSuccessHandler(logoutSuccessHandler);

        // 开启异常处理
        http.exceptionHandling()
                // 设置匿名用户处理类(未登录) 无权访问处理类(已登录 但无权限)
                .authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);

        // 开启跨域 以及对应配置
        http.cors().configurationSource(corsConfigurationSource())
                // 关闭csrf攻击
                .and().csrf().disable();

        // 开启会话管理
        http.sessionManagement()
                // 设置创建会话策略
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 设置当前用户可以创建的最大会话数量(默认1)
                .maximumSessions(Const.MAXIMUM_SESSIONS)
                // 设置会话过期处理类
                .expiredSessionStrategy(expiredSessionStrategy)
                // 设置阻止登录策略 true:禁止再次登录  false(默认):登陆时会将前一次登录的设备挤下线
                .maxSessionsPreventsLogin(true);

        // 添加jwt自动登录过滤器
        http.addFilter(jwtAuthenticationFilter())
                // 添加在UsernamePasswordAuthenticationFilter之前的captchaFilter(登陆之前)
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置认证处理器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置认证处理类(即service) 设置加密策略
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 解决添加security后 springboot自身配置的跨域不生效问题(主要由于security的过滤器优先级高于springboot的优先级)
     */
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}