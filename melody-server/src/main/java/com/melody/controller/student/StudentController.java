package com.melody.controller.student;

import com.melody.constant.JwtClaimConstant;
import com.melody.dto.StudentDTO;
import com.melody.dto.StudentLoginDTO;
import com.melody.dto.StudentWxLoginDTO;
import com.melody.entity.Student;
import com.melody.properties.JwtProperties;
import com.melody.properties.WeChatProperties;
import com.melody.result.Result;
import com.melody.service.StudentService;
import com.melody.service.impl.StudentServiceImpl;
import com.melody.utils.JwtUtil;
import com.melody.vo.StudentLoginVO;
import com.melody.vo.StudentVO;
import com.melody.vo.StudentWxLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/student/student")
@Api(tags = "学生旗下接口")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    JwtProperties jwtProperties;

    @ApiOperation("学生端登录")
    @PostMapping("/login")
    public Result<StudentLoginVO> login(@RequestBody StudentLoginDTO studentLoginDTO){
        log.info("学生端登录:{}",studentLoginDTO);

        //从数据库获取学生信息
        Student student = studentService.login(studentLoginDTO);

        //登录成功后，设置jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimConstant.STUDENT_ID,student.getId());  //利用的是主键生成的令牌
        String token = JwtUtil.createJWT(jwtProperties.getStudentSecretKey(),
                                        jwtProperties.getStudentTtl(),
                                        claims);

        //将VO传回给前端
        StudentLoginVO studentLoginVO = StudentLoginVO.builder()
                .id(student.getId())
                .username(student.getUsername())
                .name(student.getName())
                .token(token)
                .build();

        return  Result.success(studentLoginVO);
    }


    @ApiOperation("学生端修改信息")
    @PostMapping("/update")
    public Result update(@RequestBody StudentDTO studentDTO){
        log.info("编辑/修改学生信息:{}",studentDTO);
        studentService.update(studentDTO);
        return Result.success();
    }

    @ApiOperation("学生端查询个人信息")
    @GetMapping
    public Result<StudentVO> query(){
        log.info("查询学生个人信息");
        return Result.success(studentService.query());
    }


    @ApiOperation("学生端微信授权登录")
    @PostMapping("/wxLogin")
    public Result<StudentWxLoginVO> wxLogin(@RequestBody StudentWxLoginDTO studentWxLoginDTO){
        log.info("学生端微信授权登录:{}",studentWxLoginDTO);
        //从数据库获取学生信息
        Student student = studentService.wxLogin(studentWxLoginDTO);


        //将VO传回给前端
        StudentWxLoginVO studentWxLoginVO = StudentWxLoginVO.builder()
                .openid(student.getOpenid())
                .build();

        return  Result.success(studentWxLoginVO);
    }

}
