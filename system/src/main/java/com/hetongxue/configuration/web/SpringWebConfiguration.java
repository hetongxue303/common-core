package com.hetongxue.configuration.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hetongxue.security.lang.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * @Description: SpringWeb配置类
 * @ClassNmae: SpringWebConfiguration
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:00
 */
@Configuration
@EnableWebMvc
public class SpringWebConfiguration implements WebMvcConfigurer {

    @Value("${spring.jackson.time-zone}")
    private String timeZone;
    @Value("${spring.jackson.date-format}")
    private String dateFormat;
    private final String[] classpathResourceLocations = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/", "classpath:/META-INF/resources/webjars/"};

    /**
     * 资源处理器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(classpathResourceLocations);
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 添加映射路径
                .allowedHeaders("*")// 放行哪些原始请求头部信息
                .exposedHeaders("*")// 暴露哪些头部信息
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE", "TRACE", "HEAD", "PATCH")// 放行哪些请求方式
                .allowCredentials(true)// 是否发送 Cookie
                .maxAge(3600)// 最大时间
                .exposedHeaders(Const.AUTHORIZATION_KEY)
                .allowedOriginPatterns("*");
//                .allowedOriginPatterns("http://127.0.0.1:8080")
//                .allowedOriginPatterns("http://127.0.0.1:8082");
    }

    /**
     * 解决json返回时间显示时间戳问题
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = converter.getObjectMapper();
        // 生成JSON时,将所有Long转换成String
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance);
        // 时间格式化
        objectMapper.registerModule(simpleModule)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setDateFormat(new SimpleDateFormat(dateFormat))
                .setTimeZone(TimeZone.getTimeZone(timeZone));
        // 设置格式化内容
        converter.setObjectMapper(objectMapper);
        converters.add(0, converter);
    }

}