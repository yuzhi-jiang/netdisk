package com.yefeng.netdisk.front.dto;

import com.yefeng.netdisk.common.validator.group.AddGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author 夜枫
 */
@Data
public class BUser implements Serializable {
    // @NotBlank(message = "账号不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$", message = "账号应字母开头，允许5-16字节，允许字母数字下划线")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,15}$", message = "密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-15之间")
    private String password;

//    @NotBlank(message = "电话不能为空")
    @Pattern(regexp = "^1[34578]\\d{9}$", message = "电话号码不合法",groups = {AddGroup.class})
    private String mobile;

//    @NotNull(message = "性别不能为空")
//    private boolean gender;


    @NotBlank(message = "邮箱不可为空")
    @Email(message = "邮箱格式不正确")
    private String email;

}