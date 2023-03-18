package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-05 12:25
 */

//蛇形命名法
public class DiskFileVo implements Serializable {

    private static final long serialVersionUID = 12343241234324L;

//    private Long id;

    private String diskId;

    @ApiModelProperty("云盘文件/文件夹id")
    @JsonProperty("fileId")
    private String diskFileId;

    @ApiModelProperty("用户文件的名称（同样的文件每个人文件有不同的名称）")
    private String fileName;
    @ApiModelProperty("用户文件的父文件夹名称（同样的文件每个人文件有不同的路径）")
    private String parentFileId;

    @ApiModelProperty("文件类型  1(file)：文件  2(folder)：文件夹")
    private String type;

    @ApiModelProperty("文件状态0(create)待上传,1(upload).已经成功上传 2(valid).激活可用 3(invalid).不可用")
    private String status;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
