package com.yefeng.netdisk.front.util;

import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.common.verificationservice.VerificationCodeSenderLocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${verification-code.sender.type}")
    private  String type;


    @Async
    public  void send(String target, String code){
        Assert.isBlank(type,"发送方式没有设定");
        log.info("正在异步发生邮件{},code:{}",target,code);
        senderLocator.getSender(type).send(target,code);
    }

}
