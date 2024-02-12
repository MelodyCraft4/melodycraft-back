package com.melody.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置类
 */
@Component
@ConfigurationProperties(prefix = "melody.wechat")
@Data
public class WeChatProperties {

    private String appid; //小程序的appid
    private String secret; //小程序的秘钥
    private String mchid; //商户号
    private String mchSerialNo; //商户API证书的证书序列号
    private String privateKeyFilePath; //商户私钥文件
    private String apiV3Key; //证书解密的密钥
    private String domain; //微信支付地址
    private String notifyUrl; //支付成功的回调地址
    //private String refundNotifyUrl; //退款成功的回调地址

}
