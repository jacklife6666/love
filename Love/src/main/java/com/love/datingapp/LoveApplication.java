package com.love.datingapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 使用 @SpringBootApplication 注解，并显式指定要扫描的包
@SpringBootApplication(scanBasePackages = "com.love.datingapp")
// 同时，为了让MyBatis能找到Mapper接口，我们最好也加上 @MapperScan
@MapperScan("com.love.datingapp.mapper")
public class LoveApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoveApplication.class, args);
    }

}