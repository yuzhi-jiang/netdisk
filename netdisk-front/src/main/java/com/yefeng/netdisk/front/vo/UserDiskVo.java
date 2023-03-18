package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-22 13:10
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDiskVo {
    UserVo userVo;
    DiskVo diskVo;


    @Override
    public String toString() {
        return "UserDiskVo{" +
                "userVo=" + userVo +
                ", diskVo=" + diskVo +
                '}';
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public DiskVo getDiskVo() {
        return diskVo;
    }

    public void setDiskVo(DiskVo diskVo) {
        this.diskVo = diskVo;
    }
}
