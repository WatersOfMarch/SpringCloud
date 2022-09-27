package com.springcloud.alibaba.service;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


// Feign 就是接口加注解
@FeignClient(value = "nacos-payment-provider",fallback = PaymentFallbackService.class)
// 带着 Feign 注解的业务接口
public interface PaymentService {
    //调用 9003 和 9004的方法
    @GetMapping("/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
}
