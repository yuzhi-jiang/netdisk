package com.yefeng.netdisk.front.entity;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-04-02 15:23
 */
public class BaseEntity {
    @ApiModelProperty("创建人")

    private Long createUser;

    @ApiModelProperty("创建时间")

    private LocalDateTime createTime;

    @ApiModelProperty("修改人")

    private Long modifyUser;

    @ApiModelProperty("修改时间")

    private LocalDateTime modifyTime;


    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(Long modifyUser) {
        this.modifyUser = modifyUser;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
