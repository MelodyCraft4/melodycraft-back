package com.melody.controller.student;

import com.melody.result.Result;
import com.melody.service.ClassRankingService;
import com.melody.vo.ClassQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student/ranking")
@Api(tags = "学生旗下班级排行榜")
public class ClassRankingController {

    @Autowired
    ClassRankingService classRankingService;

    @GetMapping("/queryClass")
    @ApiOperation("查询班级旗下全部班级")
    public Result<List<ClassQueryVO>> queryClassById(){
        log.info("学生端-查询班级旗下全部班级");
        List<ClassQueryVO> classQueryVOS = classRankingService.queryClassById();
        return Result.success(classQueryVOS);
    }

}
