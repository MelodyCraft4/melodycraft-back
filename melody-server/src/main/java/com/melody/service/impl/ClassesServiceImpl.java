package com.melody.service.impl;

import com.melody.context.BaseContext;
import com.melody.dto.classesDTO;
import com.melody.entity.classes;
import com.melody.mapper.ClassesMapper;
import com.melody.service.ClassesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesMapper classesMapper;

    /**
     * 新增班级
     * @param classesDTO
     */
    public void saveClasses(classesDTO classesDTO) {
        classes classes = new classes();
        BeanUtils.copyProperties(classesDTO,classes);

        //TODO:待aop完善下列信息
        classes.setClassSize(0);
        classes.setClassCode(null);
        classes.setIconUrl(null);
        classes.setCreateTime(LocalDateTime.now());
        classes.setUpdateTime(LocalDateTime.now());
        classes.setCreateUser(BaseContext.getCurrentId());
        classes.setUpdateUser(BaseContext.getCurrentId());

        classesMapper.saveClasses(classes);
    }

    /**
     * 教师查询班级
     * @return
     */
    public List<classes> query() {
        //根据线程获取当前教师id
        Long id = BaseContext.getCurrentId();
        List<classes> list = classesMapper.query(id);
        return list;
    }
}
