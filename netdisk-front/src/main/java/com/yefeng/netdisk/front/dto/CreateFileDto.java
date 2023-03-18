package com.yefeng.netdisk.front.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-12 10:21
 */

public class CreateFileDto {

    @ApiModelProperty("文件是否已经存在")
    Boolean exist;
    @ApiModelProperty("是否快速上传")
    Boolean rapidUpload;

    @ApiModelProperty("上传id")
    String uploadId;

    @ApiModelProperty("文件内容hash")
    String contentHash;
    @ApiModelProperty("上传token")
    String token;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("云盘id")
    private Long diskId;

    @ApiModelProperty("云盘文件/文件夹id")
    private String diskFileId;

    @ApiModelProperty("用户文件的名称（同样的文件每个人文件有不同的名称）")
    private String fileName;

    @ApiModelProperty("用户文件的父文件夹名称（同样的文件每个人文件有不同的路径）")
    private String parentFileId;

    @ApiModelProperty("文件类型  1(file)：文件  2(folder)：文件夹")
    private String type;

        @ApiModelProperty("文件状态0(create)待上传,1(upload).已经成功上传 2(valid).激活可用 3(invalid).不可用")
    private String status;

    @ApiModelProperty("数据库文件id")
    private Long fileId;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private Long modifyUser;

    @ApiModelProperty("修改时间")
    private LocalDateTime modifyTime;


    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }

    public Boolean getRapidUpload() {
        return rapidUpload;
    }

    public void setRapidUpload(Boolean rapidUpload) {
        this.rapidUpload = rapidUpload;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(Long modifyUser) {
        this.modifyUser = modifyUser;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
