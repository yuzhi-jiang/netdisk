package com.yefeng.netdisk.front.vo;

import java.io.Serializable;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 15:51
 */

public class ShareItemVo implements Serializable {
//    {
//        "drive_id": "358565",
//
//            "file_id": "62f9ac1deb190306e1414e4c9fe043823fbeb6b4",
//            "share_id": "xSX2h1D5RHL",
//            "name": "java基础",
//            "type": "folder",
//            "created_at": "2022-08-15T02:14:53.893Z",
//            "updated_at": "2022-08-15T02:14:53.893Z",
//            "parent_file_id": "62f90b6a2173e426e8ad40a081de4c54b179c838"
//    }



    private String diskId;

    private String fileId;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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
