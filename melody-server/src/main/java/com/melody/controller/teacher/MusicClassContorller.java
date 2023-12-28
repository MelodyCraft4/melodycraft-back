package com.melody.controller.teacher;

import com.melody.context.BaseContext;
import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.result.Result;
import com.melody.service.MusicClassService;
import com.melody.utils.InviteCodeUtil;
import com.melody.vo.StudentQueryVO;
import com.melody.vo.StudentVO;
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
    public Result<String> saveClasses(@RequestBody MusicClassDTO musicClassDTO){
        log.info("新增班级");
        Long id = BaseContext.getCurrentId();//获取教师id
        String inviteCode = InviteCodeUtil.genInviteCode(id);//生成班级码
        musicClassService.saveMusicClass(musicClassDTO,inviteCode);
        return Result.success(inviteCode);
    }

    @GetMapping
    @ApiOperation("教师查询班级")
    public Result<List<MusicClass>> query(){
        log.info("教师查询班级");
        List<MusicClass> list = musicClassService.query();
        return Result.success(list);
    }

    @GetMapping("queryStudent")
    @ApiOperation("教师查询班级下的学生")
    public Result<List<StudentQueryVO>> queryStudent(@RequestParam(required = false) String name,@RequestParam Long classId){
        log.info("教师查询班级下的学生");
        List<StudentQueryVO> list = musicClassService.queryStudentByName(name,classId);
        return Result.success(list);
    }


    @GetMapping("/{id}")
    @ApiOperation("根据学生id显示学生信息")
    public Result<StudentVO> showStudent(@PathVariable Long id){
        log.info("显示学生信息");
        StudentVO studentVO = musicClassService.showStudentById(id);
        return Result.success(studentVO);
    }

    @DeleteMapping("/{studentId}/{classId}")
    @ApiOperation("根据学生id和班级id将学生移出班级")
    public Result deleteStudent(@PathVariable Long studentId,@PathVariable Long classId){
        log.info("根据学生id和班级id将学生移出班级");
        musicClassService.deleteStudentById(studentId,classId);
        return Result.success();
    }



}
