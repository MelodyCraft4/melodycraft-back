package com.melody.controller.teacher;

import com.melody.result.Result;
import com.melody.service.ClassRankingService;
import com.melody.vo.ClassQueryVO;
import com.melody.vo.ClassRankingMemberVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teacher/ranking")
@Api(tags = "教师旗下班级排行榜")
public class TeaClassRankingController {

    @Autowired
    ClassRankingService classRankingService;

    @GetMapping("/queryClass")
    @ApiOperation("查询班级旗下全部班级")
    public Result<List<ClassQueryVO>> queryClassById(){
        log.info("教师端-查询班级旗下全部班级");
        List<ClassQueryVO> classQueryVOS = classRankingService.teaQueryClassById();
        return Result.success(classQueryVOS);
    }

    @GetMapping("/list/{classId}")
    @ApiOperation("查询班级排行榜")
    public Result<List<ClassRankingMemberVO>> queryClassRanking(@PathVariable("classId") Long classId){
        log.info("教师端-查询班级排行榜");
        List<ClassRankingMemberVO> classRankingMemberVOS = classRankingService.queryClassRanking(classId);
        return Result.success(classRankingMemberVOS);
    }

}
