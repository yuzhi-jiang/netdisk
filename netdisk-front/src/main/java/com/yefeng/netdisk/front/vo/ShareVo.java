package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 13:23
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShareVo implements Serializable {
    @JsonProperty("share_id")
    private Long id;

    
    private Long diskId;

    
    private String shareTitle;

    
    private String sharePwd;
    
    private String fullShareMsg;

    
    private Long expiredTime;

    
    private Byte isValid;
    
    
    String[] fileIdList;

    @ApiModelProperty("分享的文件类型1（file）.文件2文件夹（如果有多个，则有文件夹则优先文件夹）")
     String type;

    Date expiration;

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

    public String getSharePwd() {
        return sharePwd;
    }

    public void setSharePwd(String sharePwd) {
        this.sharePwd = sharePwd;
    }

    public String getFullShareMsg() {
        return fullShareMsg;
    }

    public void setFullShareMsg(String fullShareMsg) {
        this.fullShareMsg = fullShareMsg;
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

    public String[] getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(String[] fileIdList) {
        this.fileIdList = fileIdList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
