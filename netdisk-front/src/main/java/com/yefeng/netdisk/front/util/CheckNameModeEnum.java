package com.yefeng.netdisk.front.util;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-09 11:38
 */
public enum CheckNameModeEnum {


    /**
     * 自动重命名，
     *
     */
    auto_rename((byte) 0,"auto_rename"),
    /**
     * 覆盖
     */
    overwrite((byte) 1,"overwrite"),

    /**
     * 丢弃，不用管
     */
    refuse((byte) 3,"refuse")
    ;



    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    CheckNameModeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    byte code;
    String name;


}
