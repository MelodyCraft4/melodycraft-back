package com.melody.service;

import com.melody.dto.HomeworkDTO;
import com.melody.dto.StuClassHomeworkDTO;
import com.melody.vo.HomeworkVO;
import com.melody.vo.StuClassHomeworkDetailVO;
import com.melody.vo.StuClassHomeworkVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HWService {
    /**
     * 教师发布班级作业
     * @param files
     * @param homeworkDTO
     */
    void assignhomework(List<MultipartFile> files, HomeworkDTO homeworkDTO);

    /**
     * 教师查询指定班级所有作业
     * @param classId
     * @return
     */
    List<HomeworkVO> query(Long classId);

    /**
     *  学生查询指定班级所有作业
     *  根据url上的班级id查询(班级作业表)
     */
    List<StuClassHomeworkVO> queryFromStu(Long classId);

    /**
     * 学生根据homeworkId从homework表查询作业要求,
     *    根据classHomeworkId从class_homework表查询完成情况
     * @param homeworkId
     * @param classHomeworkId
     * @return
     */
    StuClassHomeworkDetailVO queryDetailFromStu(Long homeworkId,Long classHomeworkId);

    /**
     * 学生提交作业,更新homework表
     */
    void updateHomeworkFromStu(MultipartFile file,StuClassHomeworkDTO stuClassHomeworkDTO);
}
