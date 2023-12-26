package com.melody.service.impl;

import com.melody.context.BaseContext;
import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.entity.Student;
import com.melody.mapper.MusicClassMapper;
import com.melody.service.MusicClassService;
import com.melody.vo.StudentQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MusicClassServiceImpl implements MusicClassService {

    @Autowired
    private MusicClassMapper musicClassMapper;

    /**
     * 新增班级
     * @param musicClassDTO
     */
    public void saveMusicClass(MusicClassDTO musicClassDTO,String inviteCode){
        MusicClass musicClass = new MusicClass();
        BeanUtils.copyProperties(musicClassDTO,musicClass);
        musicClass.setClassCode(inviteCode);

        //TODO:待aop完善下列信息
        musicClass.setClassSize(0);



        musicClassMapper.saveMusicClass(musicClass);
    }

    /**
     * 教师查询班级
     * @return
     */
    public List<MusicClass> query() {
        //根据线程获取当前教师id
        Long id = BaseContext.getCurrentId();
        List<MusicClass> list = musicClassMapper.query(id);
        return list;
    }

    /**
     * 教师查询班级下的学生(根据姓名)
     * @param name
     * @return
     */
    @Override
    public List<StudentQueryVO> queryStudentByName(String name,Long classId){
        if (name==null){  //前端没有传入姓名数据
            name="";
        }

        //根据姓名学生信息
        List<StudentQueryVO> list = musicClassMapper.queryStudentByName(name,classId);
        return list;
    }
}
