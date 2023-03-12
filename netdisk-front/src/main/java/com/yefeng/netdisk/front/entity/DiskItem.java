package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yefeng
 * @since 2023-03-11
 */
@TableName("tb_disk_item")
@ApiModel(value = "DiskItem对象", description = "")
public class DiskItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("隶属盘")
    private Long diskId;

    @ApiModelProperty("容量名称")
    private String capaticyName;

    @ApiModelProperty("容量大小")
    private BigDecimal capaticyValue;


    public DiskItem() {
    }

    public DiskItem(Long diskId, String capaticyName, BigDecimal capaticyValue, Integer expireTime) {
        this.diskId = diskId;
        this.capaticyName = capaticyName;
        this.capaticyValue = capaticyValue;
        this.expireTime = expireTime;
    }

    @ApiModelProperty("过期时间 -1为不过期，最低单位为天")
    private Integer expireTime;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private Long modifyUser;

    @ApiModelProperty("修改时间")
    private LocalDateTime modifyTime;

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

    public String getCapaticyName() {
        return capaticyName;
    }

    public void setCapaticyName(String capaticyName) {
        this.capaticyName = capaticyName;
    }

    public BigDecimal getCapaticyValue() {
        return capaticyValue;
    }

    public void setCapaticyValue(BigDecimal capaticyValue) {
        this.capaticyValue = capaticyValue;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
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

    @Override
    public String toString() {
        return "DiskItem{" +
            "id = " + id +
            ", diskId = " + diskId +
            ", capaticyName = " + capaticyName +
            ", capaticyValue = " + capaticyValue +
            ", expireTime = " + expireTime +
            ", createUser = " + createUser +
            ", createTime = " + createTime +
            ", modifyUser = " + modifyUser +
            ", modifyTime = " + modifyTime +
        "}";
    }
}
