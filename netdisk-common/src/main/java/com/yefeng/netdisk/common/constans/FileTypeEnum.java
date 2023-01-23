package com.yefeng.netdisk.common.constans;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-23 15:32
 */
public enum FileTypeEnum {

    /**
     * 文件夹
     */
    FOLDER(0,"folder"),
    /**
     * 文件
     */
    FILE(1,"file")
    ;

    FileTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 类型代码
     */
    private int code;

    /**
     * 类型名称
     */
    private String value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
