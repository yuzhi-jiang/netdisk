package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 13:01
 */
@TableName("tb_share_item")
@ApiModel(value = "ShareItem对象", description = "分享item表")
public class ShareItem implements Serializable {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("文件id")
    private Long fileId;

    @ApiModelProperty("分享id")
    private Long shareId;

    @ApiModelProperty("分享id")
    private Long diskId;

    @ApiModelProperty("文件类型")
    private Byte type;


    @Override
    public String toString() {
        return "ShareItem{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", shareId=" + shareId +
                ", diskId=" + diskId +
                ", type=" + type +
                '}';
    }

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long diskId) {
        this.diskId = diskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }


    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
