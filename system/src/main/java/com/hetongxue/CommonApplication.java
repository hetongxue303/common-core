package com.hetongxue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 程序入口类
 * @ClassNmae: CommonApplication
 * @Author: 何同学
 * @DateTime: 2022-07-04 15:57
 */
@SpringBootApplication
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
        System.out.println("快捷访问：http://127.0.0.1:8080");
    }

}