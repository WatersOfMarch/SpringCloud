package com.atguigu.springcloud.service;


import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
应该是建一个接口类，在再impl实现的，这里为了省事
 */
@Service
public class PaymentService {

    // 正常访问，一切ok
    public String payment_OK(Integer id) {
        return "线程池：  "+Thread.currentThread().getName()+"  payment_OK,id:  "+id+"\t"+"hhhhh";
    }

    // 服务降级注解
    // 调用方法的峰值是3s，3s以内就是正常的业务逻辑payment_Timeout；超过3s了就报错，执行兜底的业务逻辑 fallback制定的类： payment_TimeoutHandler
    @HystrixCommand(fallbackMethod = "payment_TimeoutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    // 模拟超时;假装它是个复杂的业务流程
    public String payment_Timeout(Integer id) {
        // 暂停几秒，为了触发服务降级
        int timeNumber = 5;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：  "+Thread.currentThread().getName()+"  payment_Timeout,id:  "+id+"\t"+"哈哈^_^,耗时："+timeNumber;
    }
    // payment_Timeout 服务出现问题后，找该方法
    public String payment_TimeoutHandler(Integer id) {
        return "线程池：  "+Thread.currentThread().getName()+"  系统繁忙，请稍后再试,id:  "+id+"\t"+"oT_To";
    }

    // ======服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"), //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"), //失败率达到多少跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        // 故意传一个id为负数，作为运行时异常
        if (id < 0) {
            throw new RuntimeException("******id 不能为负数");
        }
        // hutool工具类的生成唯一识别码的工具
        String serialNumber = IdUtil.simpleUUID(); // 等同于 UUID.randomUUID().toString()
        return Thread.currentThread().getName()+"\t"+"调用成功，流水号： " + serialNumber;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能负数，请稍后再试，/(ToT)/~~   id: "+id;
    }
}
