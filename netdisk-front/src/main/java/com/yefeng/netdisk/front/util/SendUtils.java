package com.yefeng.netdisk.front.util;

import com.yefeng.netdisk.common.verificationservice.VerificationCodeSenderLocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Slf4j
public class SendUtils {

//    @Value("${verification-code.sender.type}")
//    public void setType(String type) {
//        SendUtils.type =type;
//    }



    @Autowired
    private  VerificationCodeSenderLocator senderLocator;


//    @Autowired
//    public  void setSenderLocator(VerificationCodeSenderLocator senderLocator) {
//        SendUtils.senderLocator = senderLocator;
//    }



    @Async
    public  void sendEmail(String target, String code){

        log.info("正在异步发生邮件{},code:{}",target,code);
        senderLocator.getSender("email").send(target,code);
    }
    @Async
    public  void sendMobile(String target, String code){

        log.info("正在异步发送短信{},code:{}",target,code);
        senderLocator.getSender("sms").send(target,code);
    }
}
