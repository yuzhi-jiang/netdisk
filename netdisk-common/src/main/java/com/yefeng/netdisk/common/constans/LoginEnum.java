package com.yefeng.netdisk.common.constans;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 登陆类型
 *
 * @author 夜枫
 * @version 2023-01-18 18:17
 */
public enum LoginEnum {

    /**
     * 账号/邮箱 密码登录
     */
    ACCOUNT(1, "username"),
    /**
     * 手机验证码登录
     */
    MOBILE_CAPTCHA(2, "mobile_captcha"),

    MOBILE_PASS(3, "mobile_pass"),

    /**
     * 第三方登录
     */
    oauth(4, "oauth"),
    ;



    private int code;

    private String value;

    LoginEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
