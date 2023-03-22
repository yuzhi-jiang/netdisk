package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;


/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-22 13:10
 */

public class DiskVo {

//    @JsonProperty("diskId")
    private String diskId;
    private String userId;

    private BigDecimal totalCapacity;

    private BigDecimal useCapacity;


    List<DiskItemVo> diskItems;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public List<DiskItemVo> getDiskItems() {
        return diskItems;
    }

    public void setDiskItems(List<DiskItemVo> diskItems) {
        this.diskItems = diskItems;
    }
}
