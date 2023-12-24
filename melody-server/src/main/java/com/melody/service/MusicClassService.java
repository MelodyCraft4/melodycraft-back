package com.melody.service;


import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;

import java.util.List;

public interface MusicClassService {

    /**
     * 新增班级
     * @param classesDTO
     */
    void saveMusicClass(MusicClassDTO MusicClassDTO);

    /**
     * 教师查询班级
     * @return
     */
    List<MusicClass> query();
}
