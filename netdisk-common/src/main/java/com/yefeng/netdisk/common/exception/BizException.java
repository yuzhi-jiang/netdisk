package com.yefeng.netdisk.common.exception;

import lombok.Data;

/**
 * This class is for 自定义异常 业务异常
 *
 * @author 夜枫
 * @version 2023-01-01 13:17
 */
@Data

public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public BizException(String message) {
        super(message);
        this.msg = message;
    }

}
