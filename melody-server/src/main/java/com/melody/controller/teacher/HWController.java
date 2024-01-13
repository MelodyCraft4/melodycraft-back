package com.melody.controller.teacher;


import com.melody.dto.HomeworkDTO;
import com.melody.result.Result;
import com.melody.service.HWService;
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
    public Result assignhomework(@RequestPart("file") List<MultipartFile> files, HomeworkDTO homeworkDTO){
        log.info("教师发布班级作业");
        hwService.assignhomework(files,homeworkDTO);
        return Result.success();
    }

    @GetMapping("/query/{classId}")
    @ApiOperation("教师查询指定班级所有作业")
    public Result<List<HomeworkVO>> query(@PathVariable("classId") Long classId){
        log.info("教师查询指定班级所有作业");
        return Result.success(hwService.query(classId));
    }


}
