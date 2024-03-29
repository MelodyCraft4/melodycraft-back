package com.melody.controller.student;

import com.melody.constant.MessageConstant;
import com.melody.result.Result;
import com.melody.utils.HuaweiObsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RestController("studentCommonController")
@RequestMapping("/student/common")
@Api(tags = "学生旗下通用接口")
public class CommonController {

    @Autowired
    private HuaweiObsUtil huaweiObsUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestPart("file")MultipartFile file){
        log.info("学生端文件上传：{}",file.getContentType());

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();

            //截取原始文件名的后缀
            String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称：防止文件重名
            String objectName = UUID.randomUUID().toString() + extention;

            //阿里改华为
            //String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            //文件的请求路径
            String filePath = huaweiObsUtil.upload(file.getInputStream(),objectName);
            return Result.success(filePath);

        } catch (Exception e) {
            log.error("文件上传失败: {}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

    }
}
