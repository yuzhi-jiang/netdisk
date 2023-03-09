package com.yefeng.netdisk.common.verificationservice;

//@Service
public class SmsVerificationCodeSender implements VerificationCodeSender {
    @Override
    public void send(String target, String code) {
        // 调用短信发送接口发送验证码
    }
}