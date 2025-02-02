package com.yefeng.netdisk.front.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

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


//    @JsonProperty("fileId")
//    private String diskFileId;

    @ApiModelProperty("用户文件的名称（同样的文件每个人文件有不同的名称）")
    private String fileName;
    @ApiModelProperty("用户文件的父文件夹名称（同样的文件每个人文件有不同的路径）")
    private String parentFileId;

    @ApiModelProperty("文件类型  1(file)：文件  2(folder)：文件夹")
    private String type;

    @ApiModelProperty("文件状态0(create)待上传,1(upload).已经成功上传 2(valid).激活可用 3(invalid).不可用")
    private String status;


    @ApiModelProperty("云盘用户自己的显示的文件/文件夹id")
    private String fileId;




    @ApiModelProperty("文件长度/大小")
    private String length;



    @ApiModelProperty("文件hash")
    private String hash;





    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private String modifyUser;

    @ApiModelProperty("修改时间")
    private LocalDateTime modifyTime;


    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
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


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
