package com.melody.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.melody.annocation.AutoFillRedis;
import com.melody.constant.MessageConstant;
import com.melody.constant.StatusConstant;
import com.melody.context.BaseContext;
import com.melody.dto.StudentDTO;
import com.melody.dto.StudentLoginDTO;
import com.melody.dto.StudentWxLoginDTO;
import com.melody.entity.Student;
import com.melody.exception.BaseException;
import com.melody.mapper.StudentMapper;
import com.melody.properties.WeChatProperties;
import com.melody.service.StudentService;
import com.melody.utils.HttpClientUtil;
import com.melody.vo.StudentQueryVO;
import com.melody.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    WeChatProperties weChatProperties;  //微信配置属性类

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
    @AutoFillRedis
    public void update(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO,student);
        student.setSex(studentDTO.getSex().equals("男")?1:2);

        student.setId(BaseContext.getCurrentId());

        student.setUpdateTime(LocalDateTime.now());
        student.setUpdateUser(BaseContext.getCurrentId());

        student.setPassword(DigestUtils.md5DigestAsHex(student.getPassword().getBytes()));
        log.info("MD5加密后的结果：{}",student.getPassword());

        //以免传输为“”而完成了更改
        if (student.getName() == null || "".equals(student.getName())){
            student.setName(null);
        }
        if (student.getPassword() == null || "".equals(student.getPassword())){
            student.setPassword(null);
        }
        if (student.getIconUrl() == null || "".equals(student.getIconUrl())){
            student.setIconUrl(null);
        }else {
            student.setPassword(DigestUtils.md5DigestAsHex(student.getPassword().getBytes()));
        }
        if (student.getPhone() == null || "".equals(student.getPhone())){
            student.setPhone(null);
        }
        if (student.getSex() == null || "".equals(student.getSex())){
            student.setSex(null);
        }
        if (student.getSchool() == null || "".equals(student.getSchool())){
            student.setSchool(null);
        }
        if (student.getAddress() == null || "".equals(student.getAddress())){
            student.setAddress(null);
        }
        if (student.getBirthday() == null || "".equals(student.getBirthday())){
            student.setBirthday(null);
        }

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
        //性别转换
        Integer sex = student.getSex();
        if (sex != null) {
            studentVO.setSex(sex == 1 ? "男" : "女");
        } else {
            // 这里可以设置一个默认值，或者直接跳过这个操作
            studentVO.setSex("未知");
        }
        LocalDate birthday = student.getBirthday();
        int age = LocalDate.now().getYear() - birthday.getYear();
        studentVO.setAge(age);
        log.info("学生复制后数据:{}",studentVO);
        return studentVO;
    }

    /**
     * 管理端: 查询所有学生(可根据name具体查询)
     * @param name
     * @return
     */
    @Override
    public List<StudentQueryVO> queryStudentByName(String name) {
        if (name==null){  //前端没有传入姓名数据
            name="";
        }

        //根据姓名查询学生信息
        List<StudentQueryVO> list = studentMapper.queryStudentByName(name);

        return list;
    }

    /**
     * 学生端：微信授权登录
     * @param studentWxLoginDTO
     * @return
     */
    public Student wxLogin(StudentWxLoginDTO studentWxLoginDTO) {
        //调用微信接口程序，获得当前微信用户的openid
        String openid = getOpenid(studentWxLoginDTO.getCode());
        if (openid==null){
            throw new BaseException(MessageConstant.LOGIN_FAILED);
        }

        Student student = new Student();
        student.setOpenid(openid);
        student.setId(BaseContext.getCurrentId());
        //更新openid到数据库
         studentMapper.update(student);

        //返回用户对象
        return student;
    }

    /**
     * 根据学生id获取学生openid
     * @return
     */
    public String getOpenIdByStudentId(Long id) {
        String openid = studentMapper.getOpenIdByStudentId(id);
        return openid;
    }


    /**
     * 调用微信接口服务
     * @param code
     * @return
     */
    private String getOpenid(String code){
        //调用微信接口程序，获得当前微信用户的openid
        Map map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);   //微信授权码
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);//发送请求，获取
        JSONObject jsonObject = JSON.parseObject(json);//把json字符串转化为json对象

        //判断openid是否为空，为空表示登录失败，抛出业务异常
        String openid = jsonObject.getString("openid");   //获取微信接口返回的openid
        return openid;
    }


}
