package com.neuedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ShoppingApplication  extends SpringBootServletInitializer {
    //线上部署Tomcat，重写这个方法
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingApplication.class, args);
    }

}

