package com.yefeng.netdisk.common.verificationservice;

import cn.hutool.core.lang.Validator;
import cn.hutool.extra.mail.MailUtil;
import org.springframework.scheduling.annotation.Async;

//@Service
public class EmailVerificationCodeSender implements VerificationCodeSender {

    @Async("commonQueueThreadPool")
    @Override
    public void send(String target, String code) {
        // 调用邮件发送接口发送验证码
        Validator.validateEmail(target,"输入格式不是邮箱格式");
        MailUtil.send(target,"网盘邮箱验证码",getSendTemplate(code),true);
    }



    private String getSendTemplate(String code){
        StringBuilder res = new StringBuilder();
        String template = "尊敬的用户，您的验证码是 %s，请在5分钟内进行验证，请勿泄露给他人。如非本人操作，请忽略此邮件。";
        String message = String.format(template, code);

        res.append("<h4><b>邮箱验证码<b><h4>");
        res.append(message);
        return res.toString();
    }
}