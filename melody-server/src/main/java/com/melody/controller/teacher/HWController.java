package com.melody.controller.teacher;


import com.melody.dto.ClassHomeDetailDTO;
import com.melody.dto.HomeworkDTO;
import com.melody.result.Result;
import com.melody.service.HWService;
import com.melody.vo.ClassHomeworkDetailVO;
import com.melody.vo.HomeworkDetailVO;
import com.melody.vo.HomeworkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Api(tags = "教师旗下作业接口")
@Slf4j
@RequestMapping("/teacher/homework")
public class HWController {

    @Autowired
    HWService hwService;

    @PostMapping("assignhomework")
    @ApiOperation("教师发布班级作业")
    public Result assignhomework(@RequestPart(value="file",required = false) List<MultipartFile> files, HomeworkDTO homeworkDTO){
        log.info("教师发布班级作业，{}",homeworkDTO);
        hwService.assignhomework(files,homeworkDTO);
        return Result.success();
    }

    @GetMapping("/query/{classId}")
    @ApiOperation("教师查询指定班级所有作业")
    public Result<List<HomeworkVO>> query(@PathVariable("classId") Long classId){
        log.info("教师查询指定班级所有作业");
        return Result.success(hwService.query(classId));
    }

    @GetMapping("/{homeworkId}")
    @ApiOperation("教师查询指定班级作业要求")
    public Result<HomeworkDetailVO> queryHWDetailFromTea(@PathVariable("homeworkId") Long homeworkId){
        log.info("教师查询指定班级作业要求");
        return Result.success(hwService.queryHWDetailFromTea(homeworkId));
    }

    @GetMapping("/query/detail/{homeworkId}")
    @ApiOperation("教师查询指定班级作业所有同学完成情况")
    public Result<List<ClassHomeworkDetailVO>> queryClassHWDetailFromTea(@PathVariable("homeworkId") Long homeworkId){
        log.info("教师查询指定班级作业所有同学完成情况");
        return Result.success(hwService.queryClassHWDetailFromTea(homeworkId));
    }


    @PutMapping("/update")
    @ApiOperation("教师更新班级作业表信息（点评作业，退回作业")
    public Result update(@RequestBody ClassHomeDetailDTO classHomeDetailDTO){
        log.info("教师更新作业班级表信息（点评作业，退回作业");
        hwService.update(classHomeDetailDTO);
        return Result.success();
    }

}
