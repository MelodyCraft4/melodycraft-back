package com.melody.interceptor;


import com.melody.constant.JwtClaimConstant;
import com.melody.context.BaseContext;
import com.melody.properties.JwtProperties;
import com.melody.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *jwt令牌校验器
 */
@Slf4j
@Component
public class JwtTokenTeacherInterceptor implements HandlerInterceptor {

    @Autowired
    JwtProperties jwtProperties;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //从请求头中取出令牌
        String token = request.getHeader(jwtProperties.getTeacherTokenName());

        //校验令牌
        try{
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getTeacherSecretKey(), token);
            //返回的是Teacher表的主键Id
            Long teacherId = Long.valueOf(claims.get(JwtClaimConstant.TEACHER_ID).toString());
            log.info("当前教师的id：{}", teacherId);
            BaseContext.setCurrentId(teacherId);    //利用ThreadLocal技术将id存入线程空间
            //通过，放行
            return true;
        }catch (Exception ex){
            //不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
