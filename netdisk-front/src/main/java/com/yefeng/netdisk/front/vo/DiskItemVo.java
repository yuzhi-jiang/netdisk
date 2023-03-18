package com.yefeng.netdisk.front.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-18 20:20
 */

public class DiskItemVo {
    private Long id;

    @ApiModelProperty("隶属盘")
    private String diskId;

    @ApiModelProperty("容量名称")
    private String capaticyName;

    @ApiModelProperty("容量大小")
    private BigDecimal capaticyValue;

    //    @ApiModelProperty("过期时间 -1为不过期，最低单位为天")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("创建时间")
//    @JsonSerialize(using = LocalDateTimeConfig.LocalDateTimeSerializer.class)
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private String modifyUser;

    @ApiModelProperty("修改时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
