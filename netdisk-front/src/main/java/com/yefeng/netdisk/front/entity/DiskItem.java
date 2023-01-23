package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author yefeng
 * @since 2023-01-20
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

    @ApiModelProperty("过期时间 -1为不过期，最低单位为天")
    private Integer expireTime;

    public DiskItem(Long diskId, String capaticyName, BigDecimal capaticyValue, Integer expireTime) {
        this.diskId = diskId;
        this.capaticyName = capaticyName;
        this.capaticyValue = capaticyValue;
        this.expireTime = expireTime;
    }

    public DiskItem() {
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

    @Override
    public String toString() {
        return "DiskItem{" +
            "id = " + id +
            ", diskId = " + diskId +
            ", capaticyName = " + capaticyName +
            ", capaticyValue = " + capaticyValue +
            ", expireTime = " + expireTime +
        "}";
    }
}
