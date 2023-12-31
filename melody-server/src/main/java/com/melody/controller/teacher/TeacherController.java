package com.melody.controller.teacher;

import com.melody.constant.JwtClaimConstant;
import com.melody.dto.TeacherDTO;
import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;
import com.melody.properties.JwtProperties;
import com.melody.result.Result;
import com.melody.service.TeacherService;
import com.melody.utils.JwtUtil;
import com.melody.vo.TeacherLoginVO;
import com.melody.vo.TeacherVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        claims.put(JwtClaimConstant.TEACHER_ID,teacher.getId());  //利用的是主键生成的令牌
        String token = JwtUtil.createJWT(jwtProperties.getTeacherSecretKey(),
                                        jwtProperties.getTeacherTtl(),
                                        claims);

        //封装返回给前端的data数据
        TeacherLoginVO teacherLoginVO = TeacherLoginVO.builder()
                .id(teacher.getId())
                .username(teacher.getUsername())
                .name(teacher.getName())
                .token(token)
                .build();
        return Result.success(teacherLoginVO);

    }

    @ApiOperation("教师端查询个人信息")
    @GetMapping
    public Result query(){
        log.info("教师端查询个人信息");
        TeacherVO teacherVO = teacherService.query();
        return Result.success(teacherVO);
    }

    @ApiOperation("教师端更新/完善个人信息")
    @PostMapping("/update")
    public Result update(@RequestBody TeacherDTO teacherDTO){
        log.info("教师端更新/完善个人信息:{}",teacherDTO);
        teacherService.update(teacherDTO);
        return Result.success();
    }

}
