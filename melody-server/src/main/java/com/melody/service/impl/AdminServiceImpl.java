package com.melody.service.impl;

import com.melody.constant.IconUrlConstant;
import com.melody.constant.MessageConstant;
import com.melody.constant.PasswordConstant;
import com.melody.constant.StatusConstant;
import com.melody.context.BaseContext;
import com.melody.dto.AdminLoginDTO;
import com.melody.entity.Student;
import com.melody.entity.Teacher;
import com.melody.exception.BaseException;
import com.melody.mapper.AdminMapper;
import com.melody.mapper.PointsMapper;
import com.melody.mapper.StudentMapper;
import com.melody.mapper.TeacherMapper;
import com.melody.service.AdminService;
import com.melody.utils.GenAccountUtil;
import com.melody.vo.EntityVO;
import com.melody.vo.StudentRegVO;
import com.melody.vo.TeacherRegVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    PointsMapper pointsMapper;

    @Override
    public Teacher login(AdminLoginDTO adminLoginDTO) {
        //从前端传来的数据中获取教师用户名以及密码
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();


        //根据用户名密码查询数据库的数据
        Teacher teacher = teacherMapper.queryTchByUsername(username);

        //验证各种情况
        //1.用户名不存在
        if (teacher==null){
            throw new BaseException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //类型是教师
        if (teacher.getType() != 1){
            throw new BaseException(MessageConstant.ACCOUNT_TYPE_ERROR);
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


    @Override
    public EntityVO query() {
        //查询学生数量
        Integer studentNum = adminMapper.queryStudentNum();
        if (studentNum == null) {
            studentNum = 0;
        }

        //查询教师数量
        Integer teacherNum = adminMapper.queryTeacherNum();
        if (teacherNum == null) {
            teacherNum = 0;
        }

        //查询班级数量
        Integer classNum = adminMapper.queryClassNum();
        if (classNum == null) {
            classNum = 0;
        }

        EntityVO entityVO = EntityVO.builder().
                studentNum(studentNum).
                teacherNum(teacherNum).
                classNum(classNum).
                build();

        return entityVO;
    }

    /**
     * 管理员添加教师
     * @param number  添加教师的数量
     * @return
     */
    public List<TeacherRegVO> addTeacher(Integer number) {

        //判断number是否合法
        if (number == null || number <= 0) {
            throw new RuntimeException(MessageConstant.ACCOUNT_NUM_FALSE);
        }


        //查询当前教师表的记录数
        Integer teacherNum = adminMapper.queryTeacherNum();


        //如果teacherNum为null,则置为0
        if (teacherNum == null) {
            teacherNum = 0;
        }

        //封装插入数据库的数据
        List<Teacher> teacherList = new ArrayList<>();
        //封装返回给前端的数据
        List<TeacherRegVO> teacherRegVOList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            String username = GenAccountUtil.GenTeacherAccount(teacherNum + i);
            String password =  DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
            String iconUrl = IconUrlConstant.DEFAULT_ICON_URL;
            Teacher teacher = Teacher.builder().
                    username(username).
                    password(password).
                    iconUrl(iconUrl).
                    type(2).
                    createTime(LocalDateTime.now()).
                    updateTime(LocalDateTime.now()).
                    createUser(BaseContext.getCurrentId()).
                    updateUser(BaseContext.getCurrentId()).
                    build();
            teacherList.add(teacher);

            TeacherRegVO teacherRegVO = TeacherRegVO.builder().
                    username(username).
                    password(PasswordConstant.DEFAULT_PASSWORD).
                    iconUrl(iconUrl).
                    build();
            teacherRegVOList.add(teacherRegVO);
        }

        //批量插入数据库
        int result = adminMapper.addTeacher(teacherList);
        if (result < number) {
            throw new RuntimeException(MessageConstant.INSERT_ERROR);
        }
        return teacherRegVOList;
    }

    /**
     * 管理员添加学生
     * @param number  添加学生的数量
     * @return
     */
    public List<StudentRegVO> addStudent(Integer number) {
        //判断number是否合法
        if (number == null || number <= 0) {
            throw new RuntimeException(MessageConstant.ACCOUNT_NUM_FALSE);
        }


        //查询当前学生表的记录数
        Integer studentNum = adminMapper.queryStudentNum();


        //如果studentNum为null,则置为0
        if (studentNum == null) {
            studentNum = 0;
        }

        //封装插入数据库的数据
        List<Student> studentList = new ArrayList<>();
        //封装返回给前端的数据
        List<StudentRegVO> studentRegVOList = new ArrayList<>();
        //存储学生用户名数组,通过username查询学生id
        List<String> usernameList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            String username = GenAccountUtil.GenStudentAccount(studentNum + i);
            String password =  DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
            String iconUrl = IconUrlConstant.DEFAULT_ICON_URL;
            Student student = Student.builder().
                    username(username).
                    password(password).
                    iconUrl(iconUrl).
                    sex(1).
                    createTime(LocalDateTime.now()).
                    updateTime(LocalDateTime.now()).
                    createUser(BaseContext.getCurrentId()).
                    updateUser(BaseContext.getCurrentId()).
                    build();
            studentList.add(student);

            //将用户名存储进数组
            usernameList.add(username);

            StudentRegVO studentRegVO = StudentRegVO.builder().
                    username(username).
                    password(PasswordConstant.DEFAULT_PASSWORD).
                    iconUrl(iconUrl).
                    build();
            studentRegVOList.add(studentRegVO);
        }

        //批量插入数据库
        int result = adminMapper.addStudent(studentList);
        if (result < number) {
            throw new RuntimeException(MessageConstant.INSERT_ERROR);
        }

        //查询学生id,并添加进积分表
        for (String username : usernameList) {
            Long studentId = studentMapper.getIdByUsername(username);
            pointsMapper.addStudentToPointsDepot(studentId);
        }


        return studentRegVOList;
    }

    /**
     * 管理员导出学生数据
     * @param response
     */
    public void exportAccoutData(Integer type,Integer number,HttpServletResponse response) {

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=filename.xlsx");


        //1.查询数据库，获取账号数据
        //判断账号类型
        if (type == null || (type != 1 && type != 2)) {
            throw new BaseException(MessageConstant.ACCOUNT_TYPE_ERROR);
        }

        if (type == 1){  //教师账号
            log.info("-----------------查之前的Num:{}",number);
            //查询教师账号数量
            Integer teacherNum = adminMapper.queryTeacherNum();
            //判断number是否合法
            if (number == null || number <= 0 || number > teacherNum) {
                number = teacherNum;
            }
            //查询指定数量教师账号信息
            log.info("-----------------查之后的Num:{}",number);
            List<Teacher> teacherList = adminMapper.queryTeacherByNum(number);

            //2.将数据写入到excel文件中,基于模板文件创建新得excel文件
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/账号.xlsx");
            try {
                XSSFWorkbook excel = new XSSFWorkbook(in);

                //填充数据
                XSSFSheet sheet = excel.getSheet("Sheet1");
                int i=0;
                for(Teacher teacher:teacherList){
                    log.info("teacher:{}",teacher);
                    sheet.createRow(i+1).createCell(0).setCellValue(teacher.getUsername());
                    sheet.getRow(i+1).createCell(1).setCellValue(teacher.getPassword());
                    sheet.getRow(i+1).createCell(2).setCellValue(teacher.getIconUrl());
                    String name = teacher.getName()==null?"账号暂未分配":teacher.getName();
                    sheet.getRow(i+1).createCell(3).setCellValue(name);
                    sheet.getRow(i+1).createCell(4).setCellValue(teacher.getCreateTime().toString());
                    i++;

                }

                //3.将excel文件响应给前端
                ServletOutputStream outputStream = response.getOutputStream();
                excel.write(outputStream);

                //4.关闭资源
                outputStream.close();
                excel.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }else { //学生账号
            //查询学生账号数量
            Integer studentNum = adminMapper.queryStudentNum();
            //判断number是否合法
            if (number == null || number <= 0 || number > studentNum) {
                number = studentNum;
            }
            //查询指定数量学生账号信息
            List<Student> studentList = adminMapper.queryStudentByNum(number);

            //2.将数据写入到excel文件中,基于模板文件创建新得excel文件
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/账号.xlsx");
            try {
                XSSFWorkbook excel = new XSSFWorkbook(in);

                //填充数据
                XSSFSheet sheet = excel.getSheet("Sheet1");
                int i=0;
                for(Student student:studentList){
                    sheet.createRow(i+1).createCell(0).setCellValue(student.getUsername());
                    sheet.getRow(i+1).createCell(1).setCellValue(student.getPassword());
                    sheet.getRow(i+1).createCell(2).setCellValue(student.getIconUrl());
                    String name = student.getName()==null?"账号暂未分配":student.getName();
                    sheet.getRow(i+1).createCell(3).setCellValue(name);
                    sheet.getRow(i+1).createCell(4).setCellValue(student.getCreateTime().toString());
                    i++;

                }

                //3.将excel文件响应给前端
                ServletOutputStream outputStream = response.getOutputStream();
                excel.write(outputStream);

                //4.关闭资源
                outputStream.close();
                excel.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }

    }

    /**
     *  管理员重置学生密码
     */
    public void resetStuPassword(String username) {
        if (username == null || username.equals("")){
            throw new BaseException("重置密码中,username为空,传参错误");
        }
        Student student = new Student();
        student.setUsername(username);
        //默认设置为123456
        student.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        log.info("重置学生密码,{}",student);
        studentMapper.resetPasswordByUsername(student);
    }

    /**
     * 管理员重置教师密码
     */
    public void resetTchPassword(String username) {
        if (username == null || username.equals("")){
            throw new BaseException("重置密码中,username为空,传参错误");
        }
        Teacher teacher = new Teacher();
        teacher.setUsername(username);
        //默认设置为123456
        teacher.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        log.info("重置教师密码:{}",teacher);
        teacherMapper.resetPasswordByUsername(teacher);

    }


}
