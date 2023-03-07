package com.yefeng.netdisk.common.verificationservice;

public interface VerificationCodeSender {
    void send(String target, String code);
}
