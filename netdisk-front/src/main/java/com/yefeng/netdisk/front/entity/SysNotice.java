package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 通知公告表
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@TableName("sys_notice")
@ApiModel(value = "SysNotice对象", description = "通知公告表")
public class SysNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("公告ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("公告标题")
    private String noticeTitle;

    @ApiModelProperty("公告类型（1通知 2公告）")
    private Byte noticeType;

    @ApiModelProperty("公告内容")
    private String noticeContent;

    @ApiModelProperty("公告状态（0正常 1关闭）")
    private Boolean status;

    @ApiModelProperty("登录者")
    private String createUser;

    @ApiModelProperty("登录时间")
    private Long createTime;

    @ApiModelProperty("更新者")
    private String opUser;

    @ApiModelProperty("更新时间")
    private Long opTime;

    @ApiModelProperty("版本号")
    private Short lastVer;

    @ApiModelProperty("备注")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public Byte getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Byte noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public Long getOpTime() {
        return opTime;
    }

    public void setOpTime(Long opTime) {
        this.opTime = opTime;
    }

    public Short getLastVer() {
        return lastVer;
    }

    public void setLastVer(Short lastVer) {
        this.lastVer = lastVer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SysNotice{" +
            "id = " + id +
            ", noticeTitle = " + noticeTitle +
            ", noticeType = " + noticeType +
            ", noticeContent = " + noticeContent +
            ", status = " + status +
            ", createUser = " + createUser +
            ", createTime = " + createTime +
            ", opUser = " + opUser +
            ", opTime = " + opTime +
            ", lastVer = " + lastVer +
            ", remark = " + remark +
        "}";
    }
}
