package com.melody.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 华为云配置
 */
@Data
@ConfigurationProperties(prefix = "melody.huaweiobs")
@Component
public class HuaweiObsProperties {
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucketName;
}
