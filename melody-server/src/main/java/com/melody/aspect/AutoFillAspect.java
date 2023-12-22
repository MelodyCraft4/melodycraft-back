package com.melody.aspect;


import com.melody.annocation.AutoFill;
import com.melody.context.BaseContext;
import com.melody.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自动填充切面类
 */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    /**
     * 切入点：切入点满足切点表达式，并且在切入点中满足有我们自定义的注解AutoFill
     */
    @Pointcut("execution(* com.melody.mapper.*.*(..)) && @annotation(com.melody.annocation.AutoFill)")
    public void autoFillPointCut() {
    }


    /**
     * 前置通知,实现对公共字段的填充
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {  //joinPoint连接点对象，包含连接点方法、参数等信息
        log.info("开始进行公共字段的自动填充");

        //1.获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  //方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType value = autoFill.value();  //获取数据库操作类型

        //获取当前被拦截的方法的参数---实体对象:统一规定第一个参数为实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        //获取拦截方法的实体对象
        Object entity = args[0];

        //准备赋值数据
        Long currentId = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        //根据不同操作类型，为对应公共属性赋值
        if (value == OperationType.INSERT) {  //插入操作，四个公共字段均需要赋值

            //为对象的四个公共字段赋值
            try {

                //获取对象的setCreateTime方法
                Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("为对象的四个公共字段赋值失败", e);
            }

        } else if (value == OperationType.UPDATE) {
            try {

                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("为对象的两个公共字段赋值失败", e);
            }
        }


    }
}