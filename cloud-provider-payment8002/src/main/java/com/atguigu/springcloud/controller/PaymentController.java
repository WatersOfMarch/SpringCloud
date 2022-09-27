package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    // 写操作 post
    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        //  int 是因为有 useGeneratedKeys 成功返回1，失败返回0
        int result = paymentService.create(payment);
        log.info("***插入结果："+result);

        if (result > 0) {
            return new  CommonResult(200,"插入成功,serverPort  "+serverPort,result);
        } else {
            return new CommonResult(444,"插入失败",null);
        }
    }

    // 读操作 get
    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("***插入结果："+payment);

        if (payment != null) {
            return new  CommonResult(200,"查询成功,serverPort  "+serverPort,payment);
        } else {
            return new CommonResult(444,"没有对应记录,查询ID："+id,null);
        }
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }
}
