package com.yefeng.netdisk.front.entity;

import com.yefeng.netdisk.front.annotation.CreateBy;
import com.yefeng.netdisk.front.annotation.CreateTime;
import com.yefeng.netdisk.front.annotation.UpdateBy;
import com.yefeng.netdisk.front.annotation.UpdateTime;
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
    @CreateBy
    private Long createUser;

    @ApiModelProperty("创建时间")
    @CreateTime
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @UpdateBy
    private Long modifyUser;

    @ApiModelProperty("修改时间")
    @UpdateTime
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
