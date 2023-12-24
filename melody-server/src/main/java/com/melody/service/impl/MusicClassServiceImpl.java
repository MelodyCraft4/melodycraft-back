package com.melody.service.impl;

import com.melody.context.BaseContext;
import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.mapper.MusicClassMapper;
import com.melody.service.MusicClassService;
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
     * @param classesDTO
     */
    public void saveMusicClass(MusicClassDTO MusicClassDTO) {
        MusicClass MusicClass = new MusicClass();
        BeanUtils.copyProperties(MusicClassDTO,MusicClass);

        //TODO:待aop完善下列信息
        MusicClass.setClassSize(0);
        MusicClass.setClassCode(null);
        MusicClass.setIconUrl(null);
        MusicClass.setCreateTime(LocalDateTime.now());
        MusicClass.setUpdateTime(LocalDateTime.now());
        MusicClass.setCreateUser(BaseContext.getCurrentId());
        MusicClass.setUpdateUser(BaseContext.getCurrentId());

        musicClassMapper.saveMusicClass(MusicClass);
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
}
