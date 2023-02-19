package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@TableName("tb_disk_file")
@ApiModel(value = "DiskFile对象", description = "")
public class DiskFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long diskId;

    @ApiModelProperty("云盘文件/文件夹id")
    private String diskFileId;

    @ApiModelProperty("数据库文件id")
    private Long fileId;

    @Override
    public String toString() {
        return "DiskFile{" +
                "id=" + id +
                ", diskId=" + diskId +
                ", diskFileId='" + diskFileId + '\'' +
                ", fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", parentFileId='" + parentFileId + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @ApiModelProperty("用户文件的名称（同样的文件每个人文件有不同的名称）")
    private String fileName;
    @ApiModelProperty("用户文件的父文件夹名称（同样的文件每个人文件有不同的路径）")
    private String parentFileId;

    @ApiModelProperty("文件类型  1：文件  2：文件夹")
    private Byte type;

    @ApiModelProperty("文件状态0待上传,1.已经成功上传2.激活可用3.不可用")
    private Byte status;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getParentFileId() {
        return parentFileId;
    }

    public void setParentFileId(String parentFileId) {
        this.parentFileId = parentFileId;
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


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDiskFileId() {
        return diskFileId;
    }

    public void setDiskFileId(String diskFileId) {
        this.diskFileId = diskFileId;
    }

}
