package com.yefeng.netdisk.common.config;

import com.yefeng.netdisk.common.verificationservice.VerificationCodeSender;
import com.yefeng.netdisk.common.verificationservice.VerificationCodeSenderLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

//@Configuration
//public class VerificationCodeConfig {
//
//    @Autowired
//    private VerificationCodeSenderConfig senderConfig;
//
//    @Bean
//    public void initVerificationCodeSenderLocator(VerificationCodeSenderLocator locator) {
//        for (Map.Entry<String, String> entry : senderConfig.getTypes().entrySet()) {
//            try {
//                VerificationCodeSender sender = (VerificationCodeSender) Class.forName(entry.getValue()).newInstance();
//                locator.registerSender(entry.getKey(), sender);
//            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
