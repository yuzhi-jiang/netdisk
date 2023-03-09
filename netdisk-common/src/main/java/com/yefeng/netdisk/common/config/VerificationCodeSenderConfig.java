package com.yefeng.netdisk.common.config;

import com.yefeng.netdisk.common.verificationservice.EmailVerificationCodeSender;
import com.yefeng.netdisk.common.verificationservice.SmsVerificationCodeSender;
import com.yefeng.netdisk.common.verificationservice.VerificationCodeSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class VerificationCodeSenderConfig {
    @Bean("sms")
    public VerificationCodeSender smsVerificationCodeSender() {
        return new SmsVerificationCodeSender();
    }

    @Bean("email")
    public VerificationCodeSender emailVerificationCodeSender() {
        return new EmailVerificationCodeSender();
    }



    @Bean
    public Map<String, VerificationCodeSender> verificationCodeSenderMap(List<VerificationCodeSender> senders) {
        Map<String, VerificationCodeSender> verificationCodeSender = senders.stream().collect(Collectors.toMap(sender -> sender.getClass().getSimpleName().replace("VerificationCodeSender", "").toLowerCase(), sender -> sender));
        return verificationCodeSender;
    }
}

//@ConfigurationProperties(prefix = "verification-code.sender")
//public class VerificationCodeSenderConfig {
//
//    private Map<String, String> types = new HashMap<>();
//
//    public Map<String, String> getTypes() {
//        return types;
//    }
//
//    public void setTypes(Map<String, String> types) {
//        this.types = types;
//    }
//}
