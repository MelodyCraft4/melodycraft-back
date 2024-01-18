package com.melody.controller.teacher;


import com.melody.result.Result;
import com.melody.service.AdminService;
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

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/admin")
@Api(tags = "教师端管理员相关接口")
public class AdminController {

    @Autowired
    AdminService adminService;

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
    @PostMapping("/queryclassTch")
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
    @PostMapping("/queryclassStu")
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
}
