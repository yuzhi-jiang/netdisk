package com.yefeng.netdisk.common.annotation;

import java.lang.annotation.*;

/**
 * @author 夜枫
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysSaveLog {

    String value() default "";
}
