package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 分享表
 * </p>
 *
 * @author yefeng
 * @since 2023-03-11
 */
@TableName("tb_share_item")
@ApiModel(value = "Shareitem对象", description = "分享item表")
public class ShareItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 112312323432414314L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分享的云盘文件id")
    private String FileId;

    @ApiModelProperty("分享id")
    private Long shareId;

    @ApiModelProperty("文件类型")
    private Byte type;

    @ApiModelProperty("网盘id")
    private Long diskId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
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

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long diskId) {
        this.diskId = diskId;
    }


    @Override
    public String toString() {
        return "ShareItem{" +
                "id=" + id +
                ", FileId=" + FileId +
                ", shareId=" + shareId +
                ", type=" + type +
                ", diskId=" + diskId +
                '}';
    }
}
