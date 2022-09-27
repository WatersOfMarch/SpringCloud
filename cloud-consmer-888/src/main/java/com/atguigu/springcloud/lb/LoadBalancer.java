package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;


public interface LoadBalancer {
    // 得到机器的列表（有哪些微服务可以提供）
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
