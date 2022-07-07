package com.hetongxue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description: 程序入口类
 * @ClassNmae: CommonApplication
 * @Author: 何同学
 * @DateTime: 2022-07-04 15:57
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.hetongxue.system.mapper")
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }

}