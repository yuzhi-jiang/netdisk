package com.yefeng.netdisk.common.verificationservice;

import cn.hutool.core.lang.Validator;
import cn.hutool.extra.mail.MailUtil;

//@Service
public class EmailVerificationCodeSender implements VerificationCodeSender {
    @Override
    public void send(String target, String code) {
        // 调用邮件发送接口发送验证码
        Validator.validateEmail(target,"输入格式不是邮箱格式");
        MailUtil.send(target,"网盘验证码发送平台","你的验证码是"+code+"请尽快完成验证",false);
    }
}