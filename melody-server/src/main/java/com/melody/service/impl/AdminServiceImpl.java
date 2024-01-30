package com.melody.service.impl;

import com.melody.constant.IconUrlConstant;
import com.melody.constant.MessageConstant;
import com.melody.constant.PasswordConstant;
import com.melody.context.BaseContext;
import com.melody.entity.Student;
import com.melody.entity.Teacher;
import com.melody.exception.BaseException;
import com.melody.mapper.AdminMapper;
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
    @Override
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
    @Override
    public List<StudentRegVO> addStudent(Integer number) {
        //判断number是否合法
        if (number == null || number <= 0) {
            throw new RuntimeException(MessageConstant.ACCOUNT_NUM_FALSE);
        }


        //查询当前教师表的记录数
        Integer studentNum = adminMapper.queryStudentNum();


        //如果teacherNum为null,则置为0
        if (studentNum == null) {
            studentNum = 0;
        }

        //封装插入数据库的数据
        List<Student> studentList = new ArrayList<>();
        //封装返回给前端的数据
        List<StudentRegVO> studentRegVOList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            String username = GenAccountUtil.GenStudentAccount(studentNum + i);
            String password =  DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
            String iconUrl = IconUrlConstant.DEFAULT_ICON_URL;
            Student student = Student.builder().
                    username(username).
                    password(password).
                    iconUrl(iconUrl).
                    createTime(LocalDateTime.now()).
                    updateTime(LocalDateTime.now()).
                    createUser(BaseContext.getCurrentId()).
                    updateUser(BaseContext.getCurrentId()).
                    build();
            studentList.add(student);

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
        return studentRegVOList;
    }

    /**
     * 管理员导出学生数据
     * @param response
     */
    @Override
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
}
