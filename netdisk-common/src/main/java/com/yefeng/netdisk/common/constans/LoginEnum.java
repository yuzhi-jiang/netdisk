package com.yefeng.netdisk.common.constans;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-18 18:17
 */
public enum LoginEnum {

    /**
     *
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
    THIRD(4, "loginByThird"),
    ;
    private int type;

    private String value;


    LoginEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
