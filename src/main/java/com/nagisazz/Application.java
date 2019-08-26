package com.nagisazz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @auther zhushengzhe
 * @date 2019/8/23 14:07
 */
@SpringBootApplication
@MapperScan("com.nagisazz.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
