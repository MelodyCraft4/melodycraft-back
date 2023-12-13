package com.melody.controller.teacher;


import com.melody.constant.JwtClaimConstant;
import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;
import com.melody.properties.JwtProperties;
import com.melody.result.Result;
import com.melody.service.TeacherService;
import com.melody.utils.JwtUtil;
import com.melody.vo.TeacherLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/teacher/teacher")
@Api(tags = "教师相关接口")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @Autowired
    JwtProperties jwtProperties;


    @ApiOperation("教师端登录")
    @PostMapping("/login")
    public Result<TeacherLoginVO> login(@RequestBody TeacherLoginDTO teacherLoginDTO){
        log.info("教师端登录：{}",teacherLoginDTO);

        Teacher teacher = teacherService.login(teacherLoginDTO);

        //登录成功后，设置jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimConstant.TEACHER_ID,teacher.getId());
        String token = JwtUtil.createJWT(jwtProperties.getTeacherSecretKey(),
                                        jwtProperties.getTeacherTtl(),
                                        claims);

        //封装返回给前端的data数据
        TeacherLoginVO teacherLoginVO = TeacherLoginVO.builder()
                .id(teacher.getId())
                .teacherName(teacher.getTeacherName())
                .teacherId(teacher.getTeacherId())
                .token(token)
                .build();
        return Result.success(teacherLoginVO);



    }

}
