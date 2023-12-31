package com.melody.controller.teacher;


import com.melody.constant.MessageConstant;
import com.melody.result.Result;
import com.melody.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@Api(tags = "通用接口")
@Slf4j
@RequestMapping("/teacher/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestPart("file") MultipartFile file) {
        log.info("文件上传：{}", file.getContentType());

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();

            //截取原始文件名的后缀
            String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称：防止文件重名
            String objectName = UUID.randomUUID().toString() + extention;

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);

        } catch (Exception e) {
            log.error("文件上传失败: {}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

    }

}
