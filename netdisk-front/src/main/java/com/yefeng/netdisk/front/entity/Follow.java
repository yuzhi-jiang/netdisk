package com.yefeng.netdisk.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 关注用户表
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@TableName("tb_follow")
@ApiModel(value = "Follow对象", description = "关注用户表")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long fromUserId;

    @ApiModelProperty("被关注用户ID")
    private Long toUserId;

    @ApiModelProperty("是否有效")
    private Byte isValid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "Follow{" +
            "id = " + id +
            ", fromUserId = " + fromUserId +
            ", toUserId = " + toUserId +
            ", isValid = " + isValid +
        "}";
    }
}
