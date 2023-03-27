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
    FOLDER(2,"folder"),
    /**
     * 文件
     */
    FILE(1,"file")
    ;

    FileTypeEnum(int code, String value) {
        this.code = (byte) code;
        this.value = value;
    }
    FileTypeEnum(byte code, String value) {
        this.code = code;
        this.value = value;
    }
    /**
     * 类型代码
     */
    private byte code;

    /**
     * 类型名称
     */
    private String value;

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static FileTypeEnum fromCode(byte code) {
        for (FileTypeEnum status : FileTypeEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
