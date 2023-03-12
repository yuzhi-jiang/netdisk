package com.yefeng.netdisk.front.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-11 21:54
 */
@Data
public class LoginBo {
    //用户名
    @ApiModelProperty("用户名")
    private String account;
    //密码
    @ApiModelProperty("密码")
    private String password;
    //验证码
    @ApiModelProperty("验证码")
    private String captcha;
    //邮箱
    @ApiModelProperty("邮箱")
    private String email;
    //手机号
    @ApiModelProperty("手机号")
    private String mobile;



}
