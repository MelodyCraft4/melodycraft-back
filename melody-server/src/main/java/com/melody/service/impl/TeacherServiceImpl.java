package com.melody.service.impl;

import com.melody.constant.MessageConstant;
import com.melody.constant.StatusConstant;
import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;
import com.melody.exception.BaseException;
import com.melody.mapper.TeacherMapper;
import com.melody.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherMapper teacherMapper;

    /**
     * 教师端登录业务处理
     * @param teacherLoginDTO
     * @return
     */
    @Override
    public Teacher login(TeacherLoginDTO teacherLoginDTO) {
        //从前端传来的数据中获取教师用户名以及密码
        String username = teacherLoginDTO.getUsername();
        String password = teacherLoginDTO.getPassword();


        //根据用户名密码查询数据库的数据
        Teacher teacher = teacherMapper.getByUsername(username);

        //验证各种情况
        //1.用户名不存在
        if (teacher==null){
            //TODO:统一用最大的异常来处理，待完善
            throw new BaseException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //2.密码错误
        //先进行Md5加密，在进行对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("MD5加密后的结果：{}",password);
        if (!password.equals(teacher.getPassword())){
            //TODO:统一用最大的异常来处理，待完善
            throw  new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        //3.账号被锁定
        if (teacher.getStatus() == StatusConstant.DISABLE){
            //TODO:统一用最大的异常来处理，待完善
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }

        return teacher;

    }
}
