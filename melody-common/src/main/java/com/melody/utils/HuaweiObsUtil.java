package com.melody.utils;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
@Data
@AllArgsConstructor
public class HuaweiObsUtil {

    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucketName;

    /**
     * 文件上传
     *
     */
    public String upload(InputStream inputStream,String objectKey){

        log.info("bucketName:{}",bucketName);
        // 您可以通过环境变量获取访问密钥AK/SK，也可以使用其他外部引入方式传入。如果使用硬编码可能会存在泄露风险。
        // 您可以登录访问管理控制台获取访问密钥AK/SK
        String ak = accessKeyId;
        String sk = secretAccessKey;
        // 【可选】如果使用临时AK/SK和SecurityToken访问OBS，同样建议您尽量避免使用硬编码，以降低信息泄露风险。
        // 您可以通过环境变量获取访问密钥AK/SK/SecurityToken，也可以使用其他外部引入方式传入。
        // String securityToken = System.getenv("SECURITY_TOKEN");
        // endpoint填写桶所在的endpoint, 此处以华北-北京四为例，其他地区请按实际情况填写。
        String endPoint = endpoint;
        // 您可以通过环境变量获取endPoint，也可以使用其他外部引入方式传入。
        //String endPoint = System.getenv("ENDPOINT");

        // 创建ObsClient实例
        // 使用永久AK/SK初始化客户端
        ObsClient obsClient = new ObsClient(ak, sk,endPoint);
        // 使用临时AK/SK和SecurityToken初始化客户端
        // ObsClient obsClient = new ObsClient(ak, sk, securityToken, endPoint);

        try {
            // 文件上传
            // localfile为待上传的本地文件路径，需要指定到具体的文件名
            obsClient.putObject(bucketName, objectKey, inputStream);
            log.info("上传文件成功");
            obsClient.close();
        } catch (ObsException e) {
            System.out.println("putObject failed");
            // 请求失败,打印http状态码
            System.out.println("HTTP Code:" + e.getResponseCode());
            // 请求失败,打印服务端错误码
            System.out.println("Error Code:" + e.getErrorCode());
            // 请求失败,打印详细错误信息
            System.out.println("Error Message:" + e.getErrorMessage());
            // 请求失败,打印请求id
            System.out.println("Request ID:" + e.getErrorRequestId());
            System.out.println("Host ID:" + e.getErrorHostId());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("putObject failed");
            // 其他异常信息打印
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder("https://xcx-obs-1.obs.cn-south-1.myhuaweicloud.com/");
        stringBuilder
                .append(objectKey);

        log.info("文件上传至:{}",stringBuilder.toString());
        return stringBuilder.toString();

    }
}
