package com.melody.service;


import com.melody.dto.classesDTO;
import com.melody.entity.MusicClass;

import java.util.List;

public interface ClassesService {

    /**
     * 新增班级
     * @param classesDTO
     */
    void saveClasses(classesDTO classesDTO);

    /**
     * 教师查询班级
     * @return
     */
    List<MusicClass> query();
}
