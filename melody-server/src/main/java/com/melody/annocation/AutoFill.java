package com.melody.annocation;


import com.melody.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于自动填充公共字段updateTime和createTime,createUser和updateUser
 */
@Target(ElementType.METHOD)  // 表示该注解作用于方法上
@Retention(RetentionPolicy.CLASS) // 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
public @interface AutoFill {
    // 指定数据库操作类型:update insert
    OperationType value();


}
