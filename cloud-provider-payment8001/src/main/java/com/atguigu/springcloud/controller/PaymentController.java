package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

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

    // 查看微服务信息
    @GetMapping(value = "/payment/discovery")
    public Object discovery() {
        // 获取在eureka中注册好的服务有哪些
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("*****element: " + element);
        }

        // 一个微服务下面的全部具体实例(某个名称下的)
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout() {
        // 程序进来后停3s后返回
        // 暂停几秒钟线程,模拟假如服务提供端的是一个长业务需要一段时间处理
        try{
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

    @GetMapping("/payment/zipkin")
    public String paymentZipkin() {
        return "hi, I am paymentZipkin server fall back, welcome to here ^_^";
    }
}