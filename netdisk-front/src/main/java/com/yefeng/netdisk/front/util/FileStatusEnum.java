package com.yefeng.netdisk.front.util;


/**
 * 文件状态
 *
 * @author 夜枫
 * @version 2023-02-08 21:45
 */
public enum FileStatusEnum {
//    @ApiModelProperty("文件状态0待上传,1.已经成功上传2.激活可用3.不可用")

    /**
     * 创建，等待上传
     */
    create((byte)0,"create"),

    /**
     * 已经成功上传
     */
    upload((byte) 1,"upload"),
    /**
     * 激活可用
     */
    valid((byte) 2,"valid"),

    /**
     * 失效
     */
    invalid((byte) 3,"invalid")
    ;


    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    FileStatusEnum(byte code, String name) {
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

    public static FileStatusEnum fromCode(byte code) {
        for (FileStatusEnum status : FileStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }

}
