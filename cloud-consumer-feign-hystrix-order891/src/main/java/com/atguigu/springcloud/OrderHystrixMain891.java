package com.atguigu.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// 激活feign
@EnableFeignClients
@EnableHystrix
public class OrderHystrixMain891 {
    public static void main(String[] args) {
        SpringApplication.run(OrderHystrixMain891.class,args);
    }
}
