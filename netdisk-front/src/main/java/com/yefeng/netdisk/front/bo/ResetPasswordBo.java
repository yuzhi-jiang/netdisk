package com.yefeng.netdisk.front.bo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-12 11:28
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResetPasswordBo {
    private String password;
    //确认密码
    private String confirmPassword;
    private String captcha;
    private String account;
}
