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
 * jwt令牌校验的拦截器
 */
@Slf4j
@Component
public class JwtTokenStudentInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getStudentTokenName());

        //2、校验令牌
        try {
            log.info("jwt学生校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getStudentSecretKey(), token);
            Long studentId = Long.valueOf(claims.get(JwtClaimConstant.STUDENT_ID).toString());
            log.info("当前学生的id:{}", studentId);
            BaseContext.setCurrentId(studentId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }

}
