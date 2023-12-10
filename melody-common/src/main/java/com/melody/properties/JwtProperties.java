package com.melody.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 生成令牌
 */
@Component
@Data
@ConfigurationProperties("melody.jwt")
public class JwtProperties {
    /**
     * 管理端工作人员生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;
}
