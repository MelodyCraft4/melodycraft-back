package com.melody.config;


import com.alibaba.fastjson.JSONObject;
import com.melody.entity.Orders;
import com.melody.properties.WeChatProperties;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;

import java.security.Signature;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;

import java.util.ArrayList;
import java.util.Base64;

@Configuration
@Slf4j
public class WechatConfiguration {
//    public WechatConfiguration(){
//
//        //            // Load the private key from the classpath
////            ClassPathResource resource = new ClassPathResource("apiclient_key.pem");
////            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(resource.getInputStream());
//
//        Config config =
//                new RSAAutoCertificateConfig.Builder()
//                        .merchantId(weChatProperties.getMchid())
//                        .privateKeyFromPath(weChatProperties.getPrivateKeyFilePath())
//                        .merchantSerialNumber(weChatProperties.getMchSerialNo())
//                        .apiV3Key(weChatProperties.getApiV3Key())
//                        .build();
//        service =
//                new JsapiService.Builder().config(config).build();
//
//    }

    private static JsapiService service;
    private final WeChatProperties weChatProperties;

    @Autowired
    public WechatConfiguration(WeChatProperties weChatProperties){
        this.weChatProperties = weChatProperties;

        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(weChatProperties.getMchid())
                        .privateKeyFromPath(weChatProperties.getPrivateKeyFilePath())
                        .merchantSerialNumber(weChatProperties.getMchSerialNo())
                        .apiV3Key(weChatProperties.getApiV3Key())
                        .build();
        service =
                new JsapiService.Builder().config(config).build();

    }


    /** JSAPI支付下单 */
    public PrepayResponse prepay(Orders order,String openid) {
        log.info("测试进程1,此处为prepay头部");
        PrepayRequest request = new PrepayRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        request.setAppid(weChatProperties.getAppid());
        request.setMchid(weChatProperties.getMchid());
        request.setNotifyUrl(weChatProperties.getNotifyUrl()); //回调地址
        request.setDescription(order.getGoodsName());
        log.info("测试进程2");
        //金额与币种
        Amount amount = new Amount();
//        amount.setTotal(order.getAmount().intValue());
        // 测试
        amount.setTotal(1);
        amount.setCurrency("CNY");
        request.setAmount(amount);
        log.info("测试进程3");
        //prayer(openid)
        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);
        //订单号
        request.setOutTradeNo(order.getOrderNumber());
        log.info("测试进程4,此处运行到了request:{}",request);
        // 调用接口
        return service.prepay(request);
    }


    /**
     * 签名 返回JSONObject对象
     * @param prepayId
     * @return
     * @throws Exception
     */
    public JSONObject pay(String prepayId) throws Exception {
        //填充appid timestamp noncestr prepayid 并生成签名
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomStringUtils.randomNumeric(32);
        ArrayList<Object> list = new ArrayList<>();
        list.add(weChatProperties.getAppid());
        list.add(timeStamp);
        list.add(nonceStr);
        list.add("prepay_id=" + prepayId);
        //签名
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : list) {
            stringBuilder.append(o).append("\n");
        }
        String signMessage = stringBuilder.toString();
        byte[] message = signMessage.getBytes();


        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(PemUtil.loadPrivateKey(new FileInputStream(new File(weChatProperties.getPrivateKeyFilePath()))));
        signature.update(message);
        String packageSign = Base64.getEncoder().encodeToString(signature.sign());

        log.info("测试a");
        //构造数据给微信小程序，用于调起微信支付
        JSONObject jo = new JSONObject();
        jo.put("timeStamp", timeStamp);
        jo.put("nonceStr", nonceStr);
        jo.put("package", "prepay_id=" + prepayId);
        jo.put("signType", "RSA");
        jo.put("paySign", packageSign);

        log.info("测试b");
        log.info(String.valueOf(jo));

        return jo;
    }
}
