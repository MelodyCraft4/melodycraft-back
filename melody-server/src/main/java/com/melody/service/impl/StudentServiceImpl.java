package com.melody.service.impl;


import com.melody.constant.MessageConstant;
import com.melody.constant.StatusConstant;
import com.melody.context.BaseContext;
import com.melody.dto.StudentDTO;
import com.melody.dto.StudentLoginDTO;
import com.melody.entity.Student;
import com.melody.entity.Teacher;
import com.melody.exception.BaseException;
import com.melody.mapper.StudentMapper;
import com.melody.service.StudentService;
import com.melody.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    /**
     * 学生登录
     * @param studentLoginDTO
     * @return
     */
    public Student login(StudentLoginDTO studentLoginDTO) {
        //接受前端参数
        String username = studentLoginDTO.getUsername();
        String password = studentLoginDTO.getPassword();

        log.info("用户名:{}",username);
        //根据用户名密码查询数据库
        Student student = studentMapper.queryStuByUsername(username);

        //验证各种情况
        //1.用户名不存在
        if (student==null){
            throw new BaseException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //2.密码错误
        //先进行Md5加密，在进行对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("MD5加密后的结果：{}",password);
        if (!password.equals(student.getPassword())){
            throw  new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        //3.账号被锁定
        if (student.getStatus() == StatusConstant.DISABLE){
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }

        return student;
    }


    /**
     * 学生信息修改
     * @param studentDTO
     */
    public void update(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO,student);

        student.setUpdateTime(LocalDateTime.now());
        student.setUpdateUser(BaseContext.getCurrentId());

        student.setPassword(DigestUtils.md5DigestAsHex(student.getPassword().getBytes()));
        log.info("MD5加密后的结果：{}",student.getPassword());

        log.info("修改学生:{}",student);
        studentMapper.update(student);
    }

    /**
     * 学生查询个人信息
     * @return
     */
    public StudentVO query() {
        //获取个人id
        Long studentId = BaseContext.getCurrentId();
        //查询数据
        Student student= studentMapper.queryStuById(studentId);
        log.info("学生从数据库获取的信息:{}",student);
        //数据复制
        StudentVO studentVO = new StudentVO();
        BeanUtils.copyProperties(student,studentVO);
        //TODO:学生年龄需要处理
        log.info("学生复制后数据:{}",studentVO);
        return studentVO;
    }


}
