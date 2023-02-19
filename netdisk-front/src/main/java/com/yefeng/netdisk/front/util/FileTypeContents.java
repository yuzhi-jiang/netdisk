package com.yefeng.netdisk.front.util;


/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-08 21:45
 */
public enum FileTypeContents {

    /**
     * 文件
     */
    FILE((byte)1,"file"),

    /**
     * 文件夹
     */
    FOLDER((byte) 2,"folder")
    ;


    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    FileTypeContents(byte code, String name) {
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
}
