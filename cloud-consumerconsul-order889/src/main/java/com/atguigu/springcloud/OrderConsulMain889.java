package com.atguigu.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 用于当consul作为注册中心时注册服务
public class OrderConsulMain889 {
    public static void main(String[] args) {
        SpringApplication.run(OrderConsulMain889.class,args);
    }
}
