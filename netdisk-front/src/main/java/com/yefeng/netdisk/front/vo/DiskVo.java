package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yefeng.netdisk.front.entity.DiskItem;

import java.math.BigDecimal;
import java.util.List;


/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-22 13:10
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DiskVo {

    private Long id;
    private Long userId;

    private BigDecimal totalCapacity;

    private BigDecimal useCapacity;

    List<DiskItem> diskItems;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DiskVo{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalCapacity=" + totalCapacity +
                ", useCapacity=" + useCapacity +
                ", diskItems=" + diskItems +
                '}';
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

    public List<DiskItem> getDiskItems() {
        return diskItems;
    }

    public void setDiskItems(List<DiskItem> diskItems) {
        this.diskItems = diskItems;
    }
}
