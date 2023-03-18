package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-23 14:04
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileVo {
    private Long id;

    private Long diskId;

    @ApiModelProperty("文件/文件夹id")
    private Long fileId;

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

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long diskId) {
        this.diskId = diskId;
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
}
