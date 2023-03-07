package com.yefeng.netdisk.common.verificationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeSenderLocator {
    private Map<String, VerificationCodeSender> senderMap;

    @Autowired
    public VerificationCodeSenderLocator(Map<String, VerificationCodeSender> senderMap) {
        this.senderMap = senderMap;
    }

    public VerificationCodeSender getSender(String senderType) {
        VerificationCodeSender sender = senderMap.get(senderType);
        if (sender == null) {
            throw new RuntimeException("Unsupported sender type: " + senderType);
        }
        return sender;
    }
}

//public class VerificationCodeSenderLocator {
//
//    private VerificationCodeSenderLocator() {}
//
//    private static class VerificationCodeSenderLocatorHolder {
//        private static final VerificationCodeSenderLocator INSTANCE = new VerificationCodeSenderLocator();
//    }
//
//    public static VerificationCodeSenderLocator getInstance() {
//        return VerificationCodeSenderLocatorHolder.INSTANCE;
//    }
//
//    private final Map<String, VerificationCodeSender> senderMap = new ConcurrentHashMap<>();
//
//    public void registerSender(String senderType, VerificationCodeSender sender) {
//        senderMap.put(senderType, sender);
//    }
//
//    public VerificationCodeSender getSender(String senderType) {
//        return senderMap.get(senderType);
//    }
//}
