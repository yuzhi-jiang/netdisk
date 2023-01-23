package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 分享表
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@TableName("tb_share")
@ApiModel(value = "Share对象", description = "分享表")
public class Share implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户id")
    private Long fromUserId;

    @ApiModelProperty("用户名")
    private String fromUserName;

    @ApiModelProperty("文件id")
    private Long fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("过期时间")
    private Long expiredTime;

    @ApiModelProperty("是否有效")
    private Byte isValid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "Share{" +
            "id = " + id +
            ", fromUserId = " + fromUserId +
            ", fromUserName = " + fromUserName +
            ", fileId = " + fileId +
            ", fileName = " + fileName +
            ", expiredTime = " + expiredTime +
            ", isValid = " + isValid +
        "}";
    }
}
