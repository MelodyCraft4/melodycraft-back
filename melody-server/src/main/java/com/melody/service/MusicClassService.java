package com.melody.service;


import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.vo.StudentQueryVO;

import java.util.List;

public interface MusicClassService {

    /**
     * 新增班级
     * @param MusicClassDTO
     */
    void saveMusicClass(MusicClassDTO MusicClassDTO,String inviteCode);

    /**
     * 教师查询班级
     * @return
     */
    List<MusicClass> query();

    /**
     * 教师查询班级下的学生(根据姓名和班级id)
     * @param name
     * @return
     */
    List<StudentQueryVO> queryStudentByName(String name,Long classId);
}
