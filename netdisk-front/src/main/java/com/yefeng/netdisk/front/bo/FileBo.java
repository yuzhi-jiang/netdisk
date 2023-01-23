package com.yefeng.netdisk.front.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-23 14:04
 */
public class FileBo {
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty("文件类型  0：目录  1：文件")
    private Byte type;

    @ApiModelProperty("文件hash")
    private String hash;

    @ApiModelProperty("是否有效,可用于敏感文件排除")
    private Byte isValid;

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "FileBo{" +
                "id=" + id +
                ", diskId=" + diskId +
                ", fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", parentFileId='" + parentFileId + '\'' +
                ", length='" + length + '\'' +
                ", type=" + type +
                ", hash='" + hash + '\'' +
                ", isValid=" + isValid +
                '}';
    }


}
