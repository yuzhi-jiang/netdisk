package com.yefeng.netdisk.common.exception;


import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author wangjiao
 * @since 2020/11/14
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 默认的异常处理
     *
     * @param exception exception
     * @return res
     */
    @ExceptionHandler(value = {Exception.class, ArithmeticException.class,NoClassDefFoundError.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResult exceptionHandler(Exception exception) {

        log.error("exceptionHandler:exceptionName->{} | [exception:{}]", exception.getClass().getName(), exception.getMessage());
        if (Objects.nonNull(exception.getMessage())) {
            return ResultUtil.error(exception.getMessage());
        }
        return ResultUtil.error("业务发生了错误，请稍后再试吧!");
    }

    /**
     * 非法参数验证异常
     *
     * @param ex ex
     * @return res
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResult handleMethodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> list = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            list.add(fieldError.getDefaultMessage());
        }
        Collections.sort(list);
//    log.error("fieldErrors:[ex:{}]", JSON.toJSONString(list));
        return ResultUtil.unprocesableEntity();
    }


    @ExceptionHandler({TokenException.class })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ApiResult tokenExceptionHandler(ServerHttpRequest request, TokenException ex){
        log.info("ip:{},path:{},msg:{}", IPUtils.getIpAddr((HttpServletRequest) request),request.getPath(),ex.getMessage());
        return ResultUtil.error(ex.getMessage());
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResult bizExceptionHandler(ServerHttpRequest request, BizException ex){
        log.info("ip:{},path:{},msg:{}", IPUtils.getIpAddr((HttpServletRequest) request),request.getPath(),ex.getMessage());
        return ResultUtil.error(ex.getMessage());
    }
//
//  /**
//   * HTTP解析请求参数异常
//   *
//   * @param e e
//   * @return res
//   */
//  @ExceptionHandler(value = HttpMessageNotReadableException.class)
//  @ResponseStatus(HttpStatus.OK)
//  public ApiResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
//    log.error("httpMessageNotReadableException:[e:{}]", e.getMessage());
//    return ApiResult.fail(ApiCode.PARAMETER_EXCEPTION, ApiCode.PARAMETER_PARSE_EXCEPTION);
//  }
//
//  /**
//   * HTTP
//   *
//   * @param exception exception
//   * @return res
//   */
//  @ExceptionHandler(value = HttpMediaTypeException.class)
//  @ResponseStatus(HttpStatus.OK)
//  public ApiResult httpMediaTypeException(HttpMediaTypeException exception) {
//    log.error("httpMediaTypeException:[exception:{}]", exception.getMessage());
//    return ApiResult.fail(ApiCode.PARAMETER_EXCEPTION, ApiCode.HTTP_MEDIA_TYPE_EXCEPTION);
//  }
//
//  /**
//   * 自定义业务/数据异常处理
//   *
//   * @param exception exception
//   * @return res
//   */
//  @ExceptionHandler(value = {BizException.class})
//  @ResponseStatus(HttpStatus.OK)
//  public Result springBootPlusExceptionHandler(SpringBootPlusException exception) {
//    log.error("springBootPlusException:[exception:{}]", exception.getMessage());
//    int errorCode;
//    if (exception instanceof BusinessException) {
//      errorCode = ApiCode.BUSINESS_EXCEPTION.getCode();
//    } else if (exception instanceof DaoException) {
//      errorCode = ApiCode.DAO_EXCEPTION.getCode();
//    } else if (exception instanceof VerificationCodeException) {
//      errorCode = ApiCode.VERIFICATION_CODE_EXCEPTION.getCode();
//    } else {
//      errorCode = ApiCode.SPRING_BOOT_PLUS_EXCEPTION.getCode();
//    }
//    return new ApiResult().setCode(errorCode).setMsg(exception.getMessage());
//  }
//
//  /**
//   * 登陆授权异常处理
//   *
//   * @param exception exception
//   * @return res
//   */
//  @ExceptionHandler(value = AuthenticationException.class)
//  @ResponseStatus(HttpStatus.OK)
//  public ApiResult authenticationExceptionHandler(AuthenticationException exception) {
//    log.error("authenticationExceptionHandler:[exception:{}]", exception.getMessage());
//    return new ApiResult()
//        .setCode(ApiCode.AUTHENTICATION_EXCEPTION.getCode())
//        .setMsg(exception.getMessage());
//  }
//
//  /**
//   * 未认证异常处理
//   *
//   * @param exception exception
//   * @return res
//   */
//  @ExceptionHandler(value = UnauthenticatedException.class)
//  @ResponseStatus(HttpStatus.OK)
//  public ApiResult unauthenticatedExceptionHandler(UnauthenticatedException exception) {
//    log.error("unauthenticatedExceptionHandler:[exception:{}]", exception.getMessage());
//    return ApiResult.fail(ApiCode.UNAUTHENTICATED_EXCEPTION);
//  }
//
//  /**
//   * 未授权异常处理
//   *
//   * @param exception exception
//   * @return res
//   */
//  @ExceptionHandler(value = UnauthorizedException.class)
//  @ResponseStatus(HttpStatus.OK)
//  public ApiResult unauthorizedExceptionHandler(UnauthorizedException exception) {
//    log.error("unauthorizedExceptionHandler:[exception:{}]", exception.getMessage());
//    return ApiResult.fail(ApiCode.UNAUTHORIZED_EXCEPTION);
//  }
//
//  /**
//   * SQL 语法异常
//   *
//   * @param exception exception
//   * @return res
//   */
//  @ExceptionHandler(value = BadSqlGrammarException.class)
//  @ResponseStatus(HttpStatus.OK)
//  public ApiResult badSqlGrammarException(BadSqlGrammarException exception) {
//    log.info("badSqlGrammarException:[exception:{}]", exception.getMessage());
//    return ApiResult.fail(ApiCode.SQL_ERROR_EXCEPTION);
//  }

}
