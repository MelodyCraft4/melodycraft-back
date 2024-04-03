package com.melody.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // 表示该注解作用于方法上
@Retention(RetentionPolicy.RUNTIME) // 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
public @interface AutoFillRedis {
}
