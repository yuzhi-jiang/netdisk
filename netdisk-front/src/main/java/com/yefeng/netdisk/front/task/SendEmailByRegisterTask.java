package com.yefeng.netdisk.front.task;


import cn.hutool.extra.mail.MailUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * This class is for
 * 用户注册成功，发送激活链接 任务
 *
 * @author 夜枫
 * @version 2023-02-08 16:19
 */
@Slf4j
public class SendEmailByRegisterTask implements Runnable {

    Long userId;
    String email;

    Long expireTime;

    public SendEmailByRegisterTask(Long userId, String email, Long expireTime) {
        this.userId = userId;
        this.email = email;
        this.expireTime = expireTime;
    }


    @Override
    public void run() {

        // create token by email
        String token = JWTUtil.createToken(new HashMap<>() {
            {
                put("email", email);
                put("user_id", String.valueOf(userId));
                put("expire", System.currentTimeMillis() + 1000 * 60 * 60 * 24);
            }
        },expireTime);
        // send email
        log.info("发送邮件成功，token:{}",token);
        MailUtil.send(email, "网盘注册激活", "您已成功注册网盘，请点击以下链接激活账号：http://192.168.10.14:9527/front/user/"+userId+"/active?token=" + token, false);
    }



}


