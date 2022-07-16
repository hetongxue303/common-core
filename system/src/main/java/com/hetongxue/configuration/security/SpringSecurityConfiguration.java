package com.hetongxue.configuration.security;

import com.hetongxue.lang.Const;
import com.hetongxue.security.filter.CaptchaFilter;
import com.hetongxue.security.filter.JwtAuthenticationFilter;
import com.hetongxue.security.handler.*;
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
    private final UserServiceImpl userDetailsService;
    private final CaptchaFilter captchaFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登陆认证处理
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 放行白名单请求 其余请求全部拦截
        http.authorizeRequests().antMatchers(REQUEST_WHITE_LIST).permitAll().anyRequest().authenticated()
                // 设置表单登录 登录请求地址 登陆参数
                .and().formLogin().loginProcessingUrl(Const.LOGIN_PATH).usernameParameter(Const.USERNAME).passwordParameter(Const.PASSWORD)
                // 设置认证成功处理类 和 认证失败处理器
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                // 开启注销登录 注销请求地址 注销成功处理类
                .and().logout().logoutUrl(Const.LOGOUT_PATH).logoutSuccessHandler(logoutSuccessHandler)
                // 开启异常处理 匿名用户处理类(未登录) 无权限处理类(已登录 耽但无权限)
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                // 关闭session 不创建session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 开启跨域
                .and().cors()
                // 关闭csrf攻击
                .and().csrf().disable()
                // 添加在UsernamePasswordAuthenticationFilter之前的jwtAuthenticationFilter(登陆之前)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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

}