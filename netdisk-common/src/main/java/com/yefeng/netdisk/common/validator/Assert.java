package com.yefeng.netdisk.common.validator;

import com.yefeng.netdisk.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
/**
 * @author 夜枫
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new BizException(message);
        }
    }
}