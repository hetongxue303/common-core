package com.hetongxue.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @Description: http工具类
 * @ClassNmae: HttpUtil
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:11
 */
public class HttpUtil {

    /**
     * 获取request
     **/
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(servletRequestAttributes).getRequest();
    }

    /**
     * 获取response
     **/
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(servletRequestAttributes).getResponse();
    }

    /**
     * 获取session
     **/
    public static HttpSession getSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(servletRequestAttributes).getRequest().getSession();
    }

    /**
     * 获取服务器地址
     **/
    public static String getServerPath() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(servletRequestAttributes).getRequest();
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    }

}