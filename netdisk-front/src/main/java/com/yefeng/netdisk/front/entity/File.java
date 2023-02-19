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
@TableName("tb_file")
@ApiModel(value = "File对象", description = "")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("父文件夹id")
    private String fileId;

    @ApiModelProperty("源文件名")
    private String originalName;

    @ApiModelProperty("存储文件名")
    private String name;

    @ApiModelProperty("存储路径")
    private String path;

    @ApiModelProperty("文件长度/大小")
    private String length;

    @ApiModelProperty("上传id")
    private String upload_id;

    public String getUpload_id() {
        return upload_id;
    }

    public void setUpload_id(String upload_id) {
        this.upload_id = upload_id;
    }

    @ApiModelProperty("文件类型  0：目录  1：文件")
    private Byte type;

    @ApiModelProperty("文件hash")
    private String hash;

    @ApiModelProperty("是否有效,可用于敏感文件排除")
    private Byte status;

    public Long getId() {
        return id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public void setType(byte type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }


    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", fileId='" + fileId + '\'' +
                ", originalName='" + originalName + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", length='" + length + '\'' +
                ", upload_id='" + upload_id + '\'' +
                ", type=" + type +
                ", hash='" + hash + '\'' +
                ", status=" + status +
                '}';
    }
}
