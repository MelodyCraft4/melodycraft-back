package com.melody.controller.teacher;


import com.melody.constant.JwtClaimConstant;
import com.melody.dto.AdminLoginDTO;
import com.melody.entity.Teacher;
import com.melody.exception.BaseException;
import com.melody.properties.JwtProperties;
import com.melody.result.Result;
import com.melody.service.AdminService;
import com.melody.service.StudentService;
import com.melody.service.TeacherService;
import com.melody.utils.JwtUtil;
import com.melody.vo.EntityVO;
import com.melody.vo.StudentRegVO;
import com.melody.vo.TeacherRegVO;
import com.melody.service.MusicClassService;
import com.melody.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin/admin")
@Api(tags = "教师端管理员相关接口")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    MusicClassService musicClassService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    StudentService studentService;

    @Autowired
    JwtProperties jwtProperties;


    @PostMapping("/login")
    @ApiOperation("管理员端登录")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO){
        log.info("管理员端登录：{}",adminLoginDTO);
        Teacher admin = adminService.login(adminLoginDTO);
        //登录成功后，设置jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimConstant.ADMIN_ID,admin.getId());  //利用的是主键生成的令牌
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //封装返回给前端的data数据
        AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .token(token)
                .build();
        return Result.success(adminLoginVO);



    }

    @GetMapping("/query")
    @ApiOperation("管理员端查询展示在首页的数据")
    public Result<EntityVO> query(){
        log.info("管理员端查询展示在首页的数据");

        EntityVO entityVO = adminService.query();
        //返回数据
        return Result.success(entityVO);
    }

    @ApiOperation("管理员端查询班级概况")
    @GetMapping("/queryclass")
    public Result<List<MusicClassVO>> queryClass(@RequestParam(required = false) String name){
        log.info("管理员端查询班级概况");
        log.info("按班级名称搜索:{}",name);
        List<MusicClassVO> list = musicClassService.queryClassFromAdmin(name);
        return Result.success(list);
    }

    @ApiOperation("管理员端查询教师概况(班级内的单个教师)")
    @GetMapping("/queryclassTch")
    public Result<TeacherQueryVO> queryclassTch(Long classId){
        log.info("管理员端查询具体教师概况,根据班级主键:{}",classId);
        return Result.success(musicClassService.queryClassTch(classId));
    }

    @GetMapping("addTeacher")
    @ApiOperation("管理员添加教师")
    public Result<List<TeacherRegVO>> addTeacher(@RequestParam Integer number){
        log.info("管理员添加教师");
        List<TeacherRegVO> teacherRegVOList= adminService.addTeacher(number);
        return Result.success(teacherRegVOList);
    }


    @GetMapping("addStudent")
    @ApiOperation("管理员添加学生")
    public Result<List<StudentRegVO>> addStudent(@RequestParam Integer number){
        log.info("管理员添加学生");
        List<StudentRegVO> studentRegVOList= adminService.addStudent(number);
        return Result.success(studentRegVOList);
    }


    @ApiOperation("管理员端查询学生概况(班级内所有学生)")
    @GetMapping("/queryclassStu")
    public Result<List<StudentQueryVO>> queryclassStu(Long classId){
        log.info("管理员端查询学生概况");
        //调用原teacher的方法
        List<StudentQueryVO> list = musicClassService.queryStudentByName(null, classId);
        return Result.success(list);
    }

    @ApiOperation("管理员端查询学生具体信息")
    @GetMapping("/queryclassStuDetail/{studentId}")
    public Result<StudentVO> queryClassStuDetail(@PathVariable Long studentId){
        log.info("查询具体学生信息:{}",studentId);
        //调用原有teacher方法
        StudentVO studentVO = musicClassService.showStudentById(studentId);
        return Result.success(studentVO);
    }

    @ApiOperation("管理员端查询教师具体信息")
    @GetMapping("/queryclassTchDetail/{teacherId}")
    public Result<TeacherVO> queryClassTchDetail(@PathVariable Long teacherId){
        log.info("查询教师具体信息:{}",teacherId);
        TeacherVO teacherVO = musicClassService.queryClassTchDetail(teacherId);
        return Result.success(teacherVO);
    }


    @ApiOperation("查询所有教师(可根据name具体查询)")
    @GetMapping("/queryTeacher")
    public Result<List<TeacherQueryVO>> queryTeacher(@RequestParam(required = false) String name){
        log.info("查询教师");
        List<TeacherQueryVO> list = teacherService.queryTeacherByName(name);
        return Result.success(list);
    }

    @ApiOperation("查询所有学生(可根据name具体查询)")
    @GetMapping("/queryStudent")
    public Result<List<StudentQueryVO>> queryStudent(@RequestParam(required = false) String name){
        log.info("查询学生");
        List<StudentQueryVO> list = studentService.queryStudentByName(name);
        return Result.success(list);
    }

    @ApiOperation("管理员导出创建的账号")
    @GetMapping("/export")
    public void export(@RequestParam("type") Integer type,@RequestParam(value = "number",required = false) Integer number, HttpServletResponse response){
        log.info("管理员导出创建的账号");
        adminService.exportAccoutData(type,number,response);
    }

    @ApiOperation("管理员重置学生账号密码")
    @PutMapping("/resetStu/{username}")
    public Result resetStuPassword(@PathVariable String username){
        log.info("管理员重置学生账号密码,username:{}",username);
        adminService.resetStuPassword(username);
        return Result.success();
    }

    @ApiOperation("管理员重置教师账号密码")
    @PutMapping("/resetTch/{username}")
    public Result resetTchPassword(@PathVariable String username){
        log.info("管理员重置教师账号密码,username:{}",username);
        adminService.resetTchPassword(username);
        return Result.success();
    }

}
