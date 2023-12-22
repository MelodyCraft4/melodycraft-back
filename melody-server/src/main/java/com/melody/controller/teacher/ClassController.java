package com.melody.controller.teacher;

import com.melody.dto.classesDTO;
import com.melody.entity.MusicClass;
import com.melody.result.Result;
import com.melody.service.ClassesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/teacher/classes")
@Slf4j
@RestController
@Api(tags = "教师旗下班级相关接口")
public class ClassController {

    @Autowired
    private ClassesService classesService;

    @PostMapping
    @ApiOperation("教师创建班级")
    public Result saveClasses(@RequestBody classesDTO classesDTO){
        log.info("新增班级");
        classesService.saveClasses(classesDTO);
        return Result.success();
    }

    @GetMapping
    @ApiOperation("教师查询班级")
    public Result<List<MusicClass>> query(){
        log.info("教师查询班级");
        List<MusicClass> list = classesService.query();
        return Result.success(list);
    }


}
