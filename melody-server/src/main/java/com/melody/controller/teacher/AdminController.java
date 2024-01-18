package com.melody.controller.teacher;


import com.melody.result.Result;
import com.melody.service.AdminService;
import com.melody.vo.EntityVO;
import com.melody.vo.TeacherRegVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("addTeacher")
    @ApiOperation("管理员添加教师")
    public Result<List<TeacherRegVO>> addTeacher(@RequestParam Integer number){
        log.info("管理员添加教师");
        List<TeacherRegVO> teacherRegVOList= adminService.addTeacher(number);
        return Result.success(teacherRegVOList);
    }


}
