package com.hetongxue.aop.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Description: 登录日志切面
 * @ClassNmae: LogAspect
 * @Author: 何同学
 * @DateTime: 2022-07-05 19:43
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    /**
     * 切点
     */
    @Pointcut("@annotation(com.hetongxue.aop.log.LogAnnotation)")
    public void pointcut() {
    }

    /**
     * 通知
     */
    @Around("pointcut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时长
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = joinPoint.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        recordLog(joinPoint, time);
        return result;
    }

    /**
     * 记录日志
     */
    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("===============================日志开始===============================");
        log.info("日志模块：{}", logAnnotation.module());
        log.info("日志操作：{}", logAnnotation.operate());
        log.info("请求方法：{}", joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()");
        log.info("请求参数：{}", JSON.toJSONString(joinPoint.getArgs()[0]));
        log.info("请求时长：{} ms", time);
        log.info("请求IP：{}", getRequestIp());
        log.info("===============================日志结束===============================");
    }

    /**
     * 获取请求IP地址
     */
    private String getRequestIp() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        assert request != null;
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}