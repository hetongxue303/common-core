package com.hetongxue.aop.log;

import java.lang.annotation.*;

/**
 * @Description: 日志记录
 * @AnnotationNmae: LogAnnotation
 * @Author: 何同学
 * @DateTime: 2022-07-05 19:39
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operate() default "";

}