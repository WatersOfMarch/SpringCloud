package com.atguigu.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// 使用 Feign 激活并开启
@EnableFeignClients
public class OrderFeignMain890 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain890.class,args);
    }
}
