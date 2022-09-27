package com.atguigu.springcloud.service;


import org.springframework.stereotype.Component;

// 组件的注解，否则springboot容器扫描不到
@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String payment_OK(Integer id) {
        return "-----PaymentFallbackService fall back-payment_OK,  ·T_T·";
    }

    @Override
    public String payment_Timeout(Integer id) {
        return "-----PaymentFallbackService fall back-payment_Timeout, ·T_T·";
    }
}
