package com.atguigu.springcloud.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Serializable 对象序列化的接口
public class Payment implements Serializable { // 实体类序列化（后头分布式中要用到）
    private Long id;
    private String serial;
}
