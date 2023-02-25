package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

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

    @ApiModelProperty("分享云盘id")
    private Long diskId;


//    @ApiModelProperty("文件id")
//    private Long fileId;

    @ApiModelProperty("分享标题")
    private String shareTitle;

    @ApiModelProperty("分享标题")
    private String sharePwd;
    @ApiModelProperty("分享msg")
    private String fullShareMsg;

    @ApiModelProperty("过期时间")
    private String expiredTime;

    @ApiModelProperty("是否有效")
    private Byte isValid;

    @ApiModelProperty("分享类型，1.文件，2.文件夹")
    private Byte type;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getSharePwd() {
        return sharePwd;
    }

    public void setSharePwd(String sharePwd) {
        this.sharePwd = sharePwd;
    }

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

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getFullShareMsg() {
        return fullShareMsg;
    }

    public void setFullShareMsg(String fullShareMsg) {
        this.fullShareMsg = fullShareMsg;
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

    @Override
    public String toString() {
        return "Share{" +
                "id=" + id +
                ", diskId=" + diskId +
                ", shareTitle='" + shareTitle + '\'' +
                ", sharePwd='" + sharePwd + '\'' +
                ", fullShareMsg='" + fullShareMsg + '\'' +
                ", expiredTime=" + expiredTime +
                ", isValid=" + isValid +
                ", type=" + type +
                '}';
    }
}
