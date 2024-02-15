package com.melody.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 生成令牌
 */
@Component
@ConfigurationProperties("melody.jwt")
@Data
public class JwtProperties {
    /**
     * 教师端工作人员生成jwt令牌相关配置
     */
    private String teacherSecretKey;
    private long teacherTtl;
    private String teacherTokenName;

    private String studentSecretKey;
    private long studentTtl;
    private String studentTokenName;

    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

}
