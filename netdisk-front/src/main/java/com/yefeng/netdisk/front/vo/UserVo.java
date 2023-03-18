package com.yefeng.netdisk.front.vo;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-18 19:26
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1234234343423L;


    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名")
    private String username;


    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("头像")
    private String imgPath;

//    @ApiModelProperty("状态：1正常0禁用")
//    private Byte status;

    @ApiModelProperty("登录成功的token")
    public String token;

    public String DiskId;

    public String getDiskId() {
        return DiskId;
    }

    public void setDiskId(String diskId) {
        DiskId = diskId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return DesensitizedUtil.mobilePhone(this.mobile);
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
