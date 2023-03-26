package com.yefeng.netdisk.front.dto;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-22 23:12
 */
public class ShareItemDto {
    private String diskId;

    private String diskFileId;
    private String shareId;
    private String fileName;
    private String type;
    private String parentFileId;
    private String length;
    private String createUser;
    private String createTime;
    private String modifyTime;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
    public String getDiskFileId() {
        return diskFileId;
    }

    public void setDiskFileId(String diskFileId) {
        this.diskFileId = diskFileId;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentFileId() {
        return parentFileId;
    }

    public void setParentFileId(String parentFileId) {
        this.parentFileId = parentFileId;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }


}
