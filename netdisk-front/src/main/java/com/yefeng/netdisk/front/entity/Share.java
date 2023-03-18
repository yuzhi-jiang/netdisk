package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 分享表
 * </p>
 *
 * @author yefeng
 * @since 2023-03-11
 */
@TableName("tb_share")
@ApiModel(value = "Share对象", description = "分享表")
public class Share implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分享的云盘id")
    private Long diskId;

    @ApiModelProperty("用户名")
    private String fullShareMsg;

    @ApiModelProperty("文件名")
    private String shareTitle;

    @ApiModelProperty("过期时间,永久为空")
    private String expiredTime;

    @ApiModelProperty("是否有效")
    private Byte isValid;

    @ApiModelProperty("分享密码")
    private String sharePwd;

    @ApiModelProperty("分享的文件类型1.文件2文件夹（如果有多个，则有文件夹则优先文件夹）")
    private Byte type;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private Long modifyUser;

    @ApiModelProperty("修改时间")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long diskId) {
        this.diskId = diskId;
    }

    public String getFullShareMsg() {
        return fullShareMsg;
    }

    public void setFullShareMsg(String fullShareMsg) {
        this.fullShareMsg = fullShareMsg;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    public String getSharePwd() {
        return sharePwd;
    }

    public void setSharePwd(String sharePwd) {
        this.sharePwd = sharePwd;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

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

    @Override
    public String toString() {
        return "Share{" +
            "id = " + id +
            ", diskId = " + diskId +
            ", fullShareMsg = " + fullShareMsg +
            ", shareTitle = " + shareTitle +
            ", expiredTime = " + expiredTime +
            ", isValid = " + isValid +
            ", sharePwd = " + sharePwd +
            ", type = " + type +
            ", createUser = " + createUser +
            ", createTime = " + createTime +
            ", modifyUser = " + modifyUser +
            ", modifyTime = " + modifyTime +
        "}";
    }

}
