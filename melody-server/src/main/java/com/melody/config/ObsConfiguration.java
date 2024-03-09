package com.melody.config;

import com.melody.properties.HuaweiObsProperties;
import com.melody.utils.HuaweiObsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
/**
 * 华为云配置,交由bean管理
 */
public class ObsConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HuaweiObsUtil huaweiObsUtil(HuaweiObsProperties huaweiObsProperties){
        return new HuaweiObsUtil(
                huaweiObsProperties.getEndpoint(),
                huaweiObsProperties.getAccessKeyId(),
                huaweiObsProperties.getSecretAccessKey(),
                huaweiObsProperties.getBucketName()
        );
    }
}
