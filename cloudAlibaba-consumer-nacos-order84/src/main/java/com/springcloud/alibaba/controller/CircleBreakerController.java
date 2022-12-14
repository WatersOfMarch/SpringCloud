package com.springcloud.alibaba.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.springcloud.alibaba.service.PaymentService;
import jdk.nashorn.internal.ir.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@RestController
public class CircleBreakerController {
    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback") // 没有配置
//    @SentinelResource(value = "fallback", fallback = "handleFallback") // fallback只负责业务异常
//    @SentinelResource(value = "fallback", blockHandler = "blockHandler") // blockHandler只负责sentinel可视化控制台配置违规
    @SentinelResource(value = "fallback",fallback = "handleFallback", blockHandler = "blockHandler")

    public CommonResult<Payment> fallback(@PathVariable Long id) throws IllegalAccessException {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class, id);

        if (id == 4) {
            throw new IllegalAccessException("IllegalAccessException,非法参数异常。。。");
        } else if (result.getData() == null) {
            throw new NullPointerException("NullPointerException,该ID没有对应记录，空指针异常");
        }

        return result;
    }
    // 本例是 fallback
    public CommonResult handleFallback(@PathVariable Long id, Throwable e) {
        Payment payment = new Payment(id,"null");
        return new CommonResult<>(444,"兜底异常handleFallback，exception内容 " + e.getMessage(),payment);
    }
    // 本例是 blockHandler
    public CommonResult blockHandler(@PathVariable Long id, BlockException blockException) {
        Payment payment = new Payment(id,"null");
        return new CommonResult<>(445,"blockException-sentinel限流，无此流水：blockException  " + blockException.getMessage(),payment);
    }


    // ====================OpenFeign
    @Resource
    private PaymentService paymentService;

    @GetMapping("/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id) {
        return paymentService.paymentSQL(id);
    }
}
