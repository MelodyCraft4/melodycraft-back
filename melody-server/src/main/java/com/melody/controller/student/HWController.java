package com.melody.controller.student;

import com.melody.dto.StuClassHomeworkDTO;
import com.melody.result.Result;
import com.melody.service.HWService;
import com.melody.vo.StuClassHomeworkDetailVO;
import com.melody.vo.StuClassHomeworkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController("stuHWController")
@RequestMapping("student/homework")
@Api(tags = "学生旗下作业接口")
public class HWController {

    @Autowired
    HWService hwService;

    @ApiOperation("学生查询指定班级所有作业(概况)")
    @GetMapping("/{classId}")
    public Result<List<StuClassHomeworkVO>> queryFromStu(@PathVariable("classId") Long classId){
        log.info("学生查询指定班级:{}所有作业",classId);
        List<StuClassHomeworkVO> list = hwService.queryFromStu(classId);
        return Result.success(list);
    }

    @ApiOperation("学生查询班级具体作业")
    @GetMapping("/{homeworkId}/{classHomeworkId}")
    public Result<StuClassHomeworkDetailVO> queryDetailFromStu(
            @PathVariable("homeworkId") Long homeworkId,
            @PathVariable("classHomeworkId") Long classHomeworkId){
        log.info("学生查询班级具体作业,通过作业id:{},班级id:{}",homeworkId,classHomeworkId);
        StuClassHomeworkDetailVO HW = hwService.queryDetailFromStu(homeworkId,classHomeworkId);
        return Result.success(HW);
    }

    @ApiOperation("学生更新作业(提交作业)")
    @PutMapping("/commit")
    public Result update(@RequestPart("file") MultipartFile file, StuClassHomeworkDTO stuClassHomeworkDTO){
        //TODO:强制提交视频,后续还要修改
        log.info("学生:{},提交作业:{}",stuClassHomeworkDTO,file);
        //TODO:只能提交一次,除非退回,这个机制需要补充
        hwService.updateHomeworkFromStu(file,stuClassHomeworkDTO);
        return Result.success();
    }
}
