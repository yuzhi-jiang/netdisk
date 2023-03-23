package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 13:23
 */

public class ShareVo implements Serializable {
    @JsonProperty("shareId")
    private String id;

    
    private String diskId;

    
    private String shareTitle;

    
    private String sharePwd;
    
    private String fullShareMsg;

    
    private LocalDateTime expiredTime;

    
    private Byte isValid;
    private String  shareUrl;

    
    String[] fileIdList;

    @ApiModelProperty("分享的文件类型1（file）.文件2文件夹（如果有多个，则有文件夹则优先文件夹）")
     String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
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

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }



}
