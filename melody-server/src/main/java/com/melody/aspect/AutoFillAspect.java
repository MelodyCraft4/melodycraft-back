package com.melody.aspect;


import com.melody.annocation.AutoFill;
import com.melody.constant.RedisConstant;
import com.melody.context.BaseContext;
import com.melody.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Set;

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
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Pointcut("execution(* com.melody.mapper.*.*(..)) && @annotation(com.melody.annocation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Pointcut("@annotation(com.melody.annocation.AutoFillRedis)")
    public void autoFillRedisPointCut() {
    }

    /**
     * 前置通知,实现对公共字段的填充
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {  //joinPoint连接点对象，包含连接点方法、参数等信息
        log.info("开始进行公共字段的自动填充");

        //1.获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();  //方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);  //获取方法上的注解对象
        OperationType value = autoFill.value();  //获取数据库操作类型

        //2.获取到当前被拦截的方法的参数---实体对象
        Object[] args = joinPoint.getArgs();
        if (args==null || args.length==0){
            return;
        }
        Object entity = args[0];

        //3.准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //4.根据不同操作类型，为对应公共属性赋值
        if(value == OperationType.INSERT){  //插入操作，四个公共字段均需要赋值
            //获取对象的setCreateTime方法
            Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

            //通过反射为对象属性赋值
            setCreateTime.invoke(entity,now);  //调用方法
            setCreateUser.invoke(entity,currentId);
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);

        } else if (value == OperationType.UPDATE) {
            Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }

    }


    /**
     * 后置通知：清空缓存
     */
    @AfterReturning("autoFillRedisPointCut()")
    public void clearCache(JoinPoint joinPoint) {
        log.info("清空缓存");
        // 清空所有排行榜缓存
        // 获取所有以 "paihangbang" 为前缀的键
        Set<String> keys = stringRedisTemplate.keys(RedisConstant.CLASS_RANKING + "*");
        if (keys != null && !keys.isEmpty()) {
            // 删除这些键
            stringRedisTemplate.delete(keys);
        }

    }



}