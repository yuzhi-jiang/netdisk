package com.yefeng.netdisk.front.bo;

import lombok.Data;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-12 11:28
 */
@Data

public class ResetPasswordBo {
    private String password;
    //确认密码
    private String confirmPassword;
    private String captcha;
    private String account;
}
