package com.yefeng.netdisk.front.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * This class is for
 * 验证码
 *
 * @author 夜枫
 * @version 2023-01-18 19:20
 */
@Component
public class CaptchaUtil {
    @Autowired
    private RedisUtil redisUtil;

    //生成短信验证码
    public int generateCapatcha() {
        Random random = new Random();
        int max = 999999;// 最大值
        int min = 100000;// 最小值
        int code = random.nextInt(max);// 随机生成
        if (code < min) {// 低于6位数，加上最小值，补上
            code = code + min;
        }
        return code;
    }

    public String createCaptcha(String mobile) {
        //默认10分钟有效期
        return createCaptcha(mobile, 600);
    }


    public String createCaptcha(String mobile, long seconds) {
        String key = RedisKeys.getCapatchaKey(mobile);
        Object result = redisUtil.get(key);
        if (result == null) {
            String capathca = String.valueOf(generateCapatcha());
            redisUtil.setObject(key, capathca, seconds);
            return capathca;
        }
        return result.toString();
    }


    /**
     *
     * @param mobile
     * @param captcha
     * @param clearKeyByPass 验证通过是否立即删除该key
     * @return
     */
    public boolean checkCaptcha(String mobile, String captcha, boolean clearKeyByPass) {
        String key = RedisKeys.getCapatchaKey(mobile);

        boolean flag = checkCaptcha(mobile, captcha);
        if (clearKeyByPass && flag) {
            redisUtil.delete(key);
        }
        return flag;
    }

    public boolean checkCaptcha(String mobile, String captcha) {
        String key = RedisKeys.getCapatchaKey(mobile);
        Object result = redisUtil.get(key);
        if (result == null) {
            return false;
        }
        return result.toString().equals(captcha);
    }
}
