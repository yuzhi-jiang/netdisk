package com.yefeng.netdisk.front.vo;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-23 14:04
 */

public class FileVo {
    private Long id;

    private String diskId;

    @ApiModelProperty("文件/文件夹id")
    private String fileId;

    @ApiModelProperty("用户文件的名称（同样的文件每个人文件有不同的名称）")
    private String fileName;

    @ApiModelProperty("用户文件的父文件夹名称（同样的文件每个人文件有不同的路径）")
    private String parentFileId;


    @ApiModelProperty("文件长度/大小")
    private String length;



    @ApiModelProperty("文件hash")
    private String hash;

    @ApiModelProperty("是否有效,可用于敏感文件排除")
    private String status;

    @ApiModelProperty("文件类型  1(file)：文件  2(folder)：文件夹")
    private String type;



    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private String modifyUser;

    @ApiModelProperty("修改时间")
    private LocalDateTime modifyTime;


    @Override
    public String toString() {
        return "FileVo{" +
                "id=" + id +
                ", diskId=" + diskId +
                ", fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", parentFileId='" + parentFileId + '\'' +
                ", length='" + length + '\'' +
                ", type=" + type +
                ", hash='" + hash + '\'' +
                ", status=" + status +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }




    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
