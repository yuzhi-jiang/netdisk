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
public class DiskItem extends BaseEntity implements Serializable {

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

    public DiskItem(Long diskId, String capaticyName, BigDecimal capaticyValue, LocalDateTime expireTime) {
        this.diskId = diskId;
        this.capaticyName = capaticyName;
        this.capaticyValue = capaticyValue;
        this.expireTime = expireTime;
    }

    @ApiModelProperty("过期时间 -1为不过期，最低单位为天")
    private LocalDateTime expireTime;


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

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }


}
