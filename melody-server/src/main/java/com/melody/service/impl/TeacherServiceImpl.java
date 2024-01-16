package com.melody.service.impl;

import com.melody.constant.MessageConstant;
import com.melody.constant.StatusConstant;
import com.melody.context.BaseContext;
import com.melody.dto.TeacherDTO;
import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;
import com.melody.exception.BaseException;
import com.melody.mapper.TeacherMapper;
import com.melody.service.TeacherService;
import com.melody.vo.TeacherVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

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
        Teacher teacher = teacherMapper.queryTchByUsername(username);

        //验证各种情况
        //1.用户名不存在
        if (teacher==null){
            throw new BaseException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //2.密码错误
        //先进行Md5加密，在进行对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("MD5加密后的结果：{}",password);
        if (!password.equals(teacher.getPassword())){
            throw  new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        //3.账号被锁定
        if (teacher.getStatus() == StatusConstant.DISABLE){
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }

        return teacher;

    }

    /**
     * 教师端查询个人信息
     * @return
     */
    public TeacherVO query() {

        //根据线程获取当前教师Id
        Long teacherId = BaseContext.getCurrentId();
        //根据教师id到数据库查询相关信息
        Teacher teacher = teacherMapper.queryTchById(teacherId);
        //将获取到的信息转化为VO
        TeacherVO teacherVO = new TeacherVO();
        BeanUtils.copyProperties(teacher,teacherVO);

        return teacherVO;
    }

    /**
     * 教师端更新/完善个人信息
     */
    public void update(TeacherDTO teacherDTO) {
        //创建teacher,并赋值
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherDTO,teacher);
        //TODO:username字段的修改需要先查询判断重复;密码的更改,需要加密处理;电话的修改需要判断是否符合规则;status和type也需要更新(默认为0)

        //填充公共字段
        teacher.setUpdateTime(LocalDateTime.now());
        teacher.setUpdateUser(BaseContext.getCurrentId());
        //更新
        log.info("teacher:{}",teacher);
        teacherMapper.update(teacher);
    }
}
