package com.yefeng.netdisk.front.util;


/**
 * 文件状态
 *
 * @author 夜枫
 * @version 2023-02-08 21:45
 */
public enum ShareStatusEnum {
    /**
     * 失效
     */
    invalid((byte) 0,"invalid"),


    /**
     * 激活可用
     */
    valid((byte) 1,"valid");




    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    ShareStatusEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    byte code;
    String name;

    public static ShareStatusEnum fromCode(byte code) {
        for (ShareStatusEnum status : ShareStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }

}
