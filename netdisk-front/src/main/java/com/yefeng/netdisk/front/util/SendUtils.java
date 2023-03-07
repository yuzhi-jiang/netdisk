package com.yefeng.netdisk.front.util;

import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.common.verificationservice.VerificationCodeSenderLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class SendUtils {
    @Autowired
    private static VerificationCodeSenderLocator senderLocator;

    @Value("${verification-code.sender.type}")
    private static String type;

    public static void send(String target, String code){
        Assert.isBlank(type,"发送方式没有设定");
        senderLocator.getSender(type).send(target,code);
    }

}
