package com.yefeng.netdisk.front.aspect;

import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-20 14:00
 */
@RestControllerAdvice(basePackages = {"com.yefeng.netdisk.front.controller"})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class FrontExceptionHandler {

    @ExceptionHandler(value = { DuplicateKeyException.class, SQLIntegrityConstraintViolationException.class})
    public ApiResult SQLIntegrityConstraintViolationException(Exception ex) {
        log.error("FrontExceptionHandler: exceptionName->[{}] | [exception:{}]", ex.getClass().getName(), ex.getMessage());

        Throwable cause = ex.getCause();
        if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
            String localizedMessage = ex.getLocalizedMessage();
            String message = ex.getMessage();


            String sqlState = ((SQLIntegrityConstraintViolationException) cause).getSQLState();
            String errMsg = cause.getMessage();

            List<String> repeatField = new ArrayList<String>(3);
            if (StringUtils.isNotBlank(errMsg)) {
                if (errMsg.contains("username")) {
                    repeatField.add("username");
                } else if (errMsg.contains("mobile")) {

                    repeatField.add("mobile");
                } else if (errMsg.contains("email")) {

                    repeatField.add("email");
                }
            }
            log.info("表字段{}重复了", repeatField);
            log.info("SQLIntegrityConstraintViolationException ：" + ex.getMessage());
//            return new ApiResult(HttpCodeEnum.UNPROCESABLE_ENTITY.getCode(), "表单" + Arrays.toString(repeatField.toArray()) + "字段已经被使用了");
            return new ApiResult(HttpCodeEnum.UNPROCESABLE_ENTITY.getCode(),   Arrays.toString(repeatField.toArray()) + "已经被使用");
        }
        return new ApiResult(HttpCodeEnum.FAIL.getCode(), "数据库连接失败");
    }

    @ExceptionHandler(value = {CannotGetJdbcConnectionException.class,BadSqlGrammarException.class})
    public ApiResult BadSqlGrammarException(Exception ex){
        log.error("BadSqlGrammarException msg:{}",ex.getMessage());
        ex.printStackTrace();
        return new ApiResult(HttpCodeEnum.FAIL.getCode(),"数据库业务出现异常");
    }
    @ExceptionHandler(value = {MissingServletRequestPartException.class})
    public ApiResult MissingServletRequestPartException(Exception ex){
        log.error("MissingServletRequestPartException msg:{}",ex.getMessage());
        ex.printStackTrace();
        return new ApiResult(HttpCodeEnum.FAIL.getCode(),"请检查参数正确");
    }


}
