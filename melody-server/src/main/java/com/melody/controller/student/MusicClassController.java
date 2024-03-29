package com.melody.controller.student;

import com.melody.result.Result;
import com.melody.service.MusicClassService;
import com.melody.vo.MusicClassVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController("studentMusicClassController")
@RequestMapping("/student/class")
@Api(tags = "学生旗下班级接口")
public class MusicClassController {

    @Autowired
    private MusicClassService musicClassService;

    @ApiOperation("学生加入班级")
    @PostMapping("/join")
    public Result joinClass(String classCode){
        log.info("学生加入班级,班级码:{}",classCode);
        musicClassService.joinClass(classCode);
        return Result.success();
    }

    @ApiOperation("学生查询班级")
    @GetMapping("/query")
    public Result<List<MusicClassVO>> query(){
        log.info("学生查询班级");
        List<MusicClassVO> list = musicClassService.queryByStudentId();
        return Result.success(list);
    }
}
