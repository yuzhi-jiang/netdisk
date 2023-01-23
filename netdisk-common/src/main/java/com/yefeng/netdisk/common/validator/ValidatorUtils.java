package com.yefeng.netdisk.common.validator;


import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.exception.CheckFailException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * 校验工具类
 * @author 夜枫
 *
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws CheckFailException  校验不通过，则报CheckFailException异常
     */
    public static void validateEntity(Object object, Class<?>... groups) throws BizException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
        	ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            throw new CheckFailException(constraint.getMessage());
        }
    }
}
