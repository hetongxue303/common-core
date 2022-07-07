package com.hetongxue.configuration.security;

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

    private final String[] WHITE_LIST = {"/login", "/getVerify"};
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomizeLogoutSuccessHandler logoutSuccessHandler;
    private final CustomizeAccessDeniedHandler accessDeniedHandler;
    private final CustomizeAuthenticationEntryPoint authenticationEntryPoint;
    private final UserServiceImpl userDetailsService;
    private final CaptchaFilter captchaFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 登陆认证处理
        http.authorizeRequests().antMatchers(WHITE_LIST).permitAll().anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password").successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler)
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors()
                .and().csrf().disable()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置认证处理器
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}