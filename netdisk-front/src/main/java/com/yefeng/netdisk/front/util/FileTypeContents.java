package com.yefeng.netdisk.front.util;


/**
 * 文件类型
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

    public static FileTypeContents fromCode(byte code) {
        for (FileTypeContents status : FileTypeContents.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
