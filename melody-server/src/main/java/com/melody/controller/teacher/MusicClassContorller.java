package com.melody.controller.teacher;

import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.result.Result;
import com.melody.service.MusicClassService;
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
public class MusicClassContorller {

    @Autowired
    private MusicClassService musicClassService;

    @PostMapping
    @ApiOperation("教师创建班级")
    public Result saveClasses(@RequestBody MusicClassDTO musicClassDTO){
        log.info("新增班级");
        musicClassService.saveMusicClass(musicClassDTO);
        return Result.success();
    }

    @GetMapping
    @ApiOperation("教师查询班级")
    public Result<List<MusicClass>> query(){
        log.info("教师查询班级");
        List<MusicClass> list = musicClassService.query();
        return Result.success(list);
    }


}
