package com.melody.service;

import com.melody.dto.HomeworkDTO;
import com.melody.vo.HomeworkVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HWService {
    void assignhomework(List<MultipartFile> files, HomeworkDTO homeworkDTO);

    List<HomeworkVO> query(Long classId);
}
