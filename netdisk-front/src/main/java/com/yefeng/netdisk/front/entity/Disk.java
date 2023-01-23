package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@TableName("tb_disk")
@ApiModel(value = "Disk对象", description = "")
public class Disk implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private BigDecimal totalCapacity;

    private BigDecimal useCapacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(BigDecimal totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public BigDecimal getUseCapacity() {
        return useCapacity;
    }

    public void setUseCapacity(BigDecimal useCapacity) {
        this.useCapacity = useCapacity;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalCapacity=" + totalCapacity +
                ", useCapacity=" + useCapacity +
                '}';
    }
}
