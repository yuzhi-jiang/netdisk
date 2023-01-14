package com.yefeng.netdisk.common.exception;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-14 14:23
 */
public class TokenException extends RuntimeException {

    private static final long serialVersionUID =23234234L;

    private String msg;
    private int code = 403;

    public TokenException(String message) {
        super(message);
        this.msg = message;
    }

}
