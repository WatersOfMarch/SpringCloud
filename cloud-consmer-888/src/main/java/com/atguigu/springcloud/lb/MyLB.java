package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class MyLB implements LoadBalancer {

    // 提供一个原子类，初始值为0
    // 原子类可在高并发场景下高效处理程序；并保证线程安全(Java语言中，++i和i++操作并不是线程安全的)
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    // 得到并增加
    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1; // 2147483647:int的最大值
        }while (!this.atomicInteger.compareAndSet(current,next)); // 自旋，一直取到想要的值为之
        System.out.println("******第几次访问，next： "+next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        // getAndIncrement 代表第几次访问
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
