package com.melody.contorller.admin.teacher;


import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;
import com.melody.result.Result;
import com.melody.service.TeacherService;
import com.melody.vo.TeacherLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/teacher/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

//    @PostMapping("/login")
//    @ApiOperation("教师端登录")
//    public Result<TeacherLoginVO> login(@RequestBody TeacherLoginDTO teacherLoginDTO){
//        log.info("教师端登录：{}",teacherLoginDTO);
//
//        Teacher teacher = teacherService.login(teacherLoginDTO);
//
//
//
//    }

}
