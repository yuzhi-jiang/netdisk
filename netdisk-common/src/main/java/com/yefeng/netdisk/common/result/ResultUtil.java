package com.yefeng.netdisk.common.result;

/**
 * 返回结果工具类
 * @author 夜枫
 */
public class ResultUtil {

	/**
     * 请求成功
     * @return
     */
    public static ApiResult success() {
        return new ApiResult(HttpCodeEnum.OK.getCode(),HttpCodeEnum.OK.getMessage());
    }

    /**
     * 请求成功（无消息）
     * @return
     */
    public static ApiResult successAndNoMsg() {
        return new ApiResult(HttpCodeEnum.OK.getCode(),"");
    }

    /**
     * 成功请求
     *
     * @param data
     * @return
     */
    public static   ApiResult  success(Object data) {
        return new ApiResult(HttpCodeEnum.OK.getCode(), HttpCodeEnum.OK.getMessage() , data);
    }

    /**
     * 成功请求（无消息）
     *
     * @param data
     * @return
     */
    public static ApiResult successAndNoMsg(Object data) {
        return new ApiResult(HttpCodeEnum.OK.getCode(), "", data);
    }

    /**
     * 操作失败
     * @return
     */
    public static ApiResult fail() {
        return new ApiResult(HttpCodeEnum.FAIL.getCode(), HttpCodeEnum.FAIL.getMessage());
    }
    public static ApiResult failMsg(String msg) {
        return new ApiResult(HttpCodeEnum.FAIL.getCode(), msg);
    }
    /**
     * 操作失败
     * @return
     */
    public static ApiResult fail(Object data) {
        return new ApiResult(HttpCodeEnum.FAIL.getCode(), HttpCodeEnum.FAIL.getMessage() ,data);
    }
    
    /**
     * 服务器错误
     * @return
     */
    public static ApiResult error() {
        return new ApiResult(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(),HttpCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
    }


    /**
     * 服务器错误
     * @param data
     * @return
     */
    public static ApiResult error(Object data) {
        return new ApiResult(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(),HttpCodeEnum.INTERNAL_SERVER_ERROR.getMessage(), data);
    }
    /**
     * 参数错误
     * @return
     */
    public static ApiResult paramError() {
        return new ApiResult(HttpCodeEnum.INVALID_REQUEST.getCode(), HttpCodeEnum.INVALID_REQUEST.getMessage());
    }

    /**
     * 参数错误
     * @param data
     * @return
     */
    public static ApiResult paramError(Object data) {
        return new ApiResult(HttpCodeEnum.INVALID_REQUEST.getCode(), HttpCodeEnum.INVALID_REQUEST.getMessage(), data);
    }

    /**
     * 认证到期
     * @return
     */
    public static ApiResult authExpired() {
        return new ApiResult(HttpCodeEnum.AUTH_EXPIRED.getCode(), HttpCodeEnum.AUTH_EXPIRED.getMessage());
    }

    /**
     * 没有权限
     * @return
     */
    public static ApiResult unAuthorized() {
        return new ApiResult(HttpCodeEnum.UNAUTHORIZED.getCode(), HttpCodeEnum.UNAUTHORIZED.getMessage());
    }

    /**
     * 没有权限
     * @param data
     * @return
     */
    public static ApiResult unAuthorized(Object data) {
        return new ApiResult(HttpCodeEnum.UNAUTHORIZED.getCode(),HttpCodeEnum.UNAUTHORIZED.getMessage(),data);
    }


    /**
     *  禁止访问
     * @return
     */
    public static ApiResult forbidden() {
        return new ApiResult(HttpCodeEnum.FORBIDDEN.getCode(),HttpCodeEnum.FORBIDDEN.getMessage());
    }

    /**
     * 禁止访问
     * @param data
     * @return
     */
    public static ApiResult forbidden(Object data) {
        return new ApiResult(HttpCodeEnum.FORBIDDEN.getCode(),HttpCodeEnum.FORBIDDEN.getMessage(), data);
    }


    /**
     * 资源不存在
     * @return
     */
    public static ApiResult notFound() {
        return new ApiResult(HttpCodeEnum.NOT_FOUND.getCode(),HttpCodeEnum.NOT_FOUND.getMessage());
    }


    /**
     * 资源不存在
     * @param data
     * @return
     */
    public static ApiResult notFound(Object data) {
        return new ApiResult(HttpCodeEnum.NOT_FOUND.getCode(),HttpCodeEnum.NOT_FOUND.getMessage(), data);
    }


    /**
     * 请求的格式不正确
     * @return
     */
    public static ApiResult notAcceptable() {
        return new ApiResult(HttpCodeEnum.NOT_ACCEPTABLE.getCode(),HttpCodeEnum.NOT_ACCEPTABLE.getMessage());
    }


    /**
     * 请求的格式不正确
     * @param data
     * @return
     */
    public static ApiResult notAcceptable(Object data) {
        return new ApiResult(HttpCodeEnum.NOT_ACCEPTABLE.getCode(),HttpCodeEnum.NOT_ACCEPTABLE.getMessage(), data);
    }


    /**
     * 数据已经被删除
     * @return
     */
    public static ApiResult gone() {
        return new ApiResult(HttpCodeEnum.GONE.getCode(),HttpCodeEnum.GONE.getMessage());
    }


    /**
     * 数据已经被删除
     * @param data
     * @return
     */
    public static ApiResult gone(Object data) {
        return new ApiResult(HttpCodeEnum.GONE.getCode(),HttpCodeEnum.GONE.getMessage(), data);
    }

    /**
     * 实体参数校验错误
     * @return
     */
    public static ApiResult unprocesableEntity() {
        return new ApiResult(HttpCodeEnum.UNPROCESABLE_ENTITY.getCode(),HttpCodeEnum.UNPROCESABLE_ENTITY.getMessage());
    }

    /**
     * 实体参数校验错误
     * @param data
     * @return
     */
    public static ApiResult unprocesableEntity(Object data) {
        return new ApiResult(HttpCodeEnum.UNPROCESABLE_ENTITY.getCode(),HttpCodeEnum.UNPROCESABLE_ENTITY.getMessage(), data);
    }

    /**
     * 未知错误
     * @return
     */
    public static ApiResult unKnowError() {
        return new ApiResult(HttpCodeEnum.UN_KNOW_ERROR.getCode(),HttpCodeEnum.UN_KNOW_ERROR.getMessage());
    }

    /**
     * 未知错误
     * @param data
     * @return
     */
    public static ApiResult unKnowError(Object data) {
        return new ApiResult(HttpCodeEnum.UN_KNOW_ERROR.getCode(),HttpCodeEnum.UN_KNOW_ERROR.getMessage(), data);
    }

    /**
     * 业务逻辑验证未通过
     * @return
     */
    public static ApiResult verificationFailed() {
        return new ApiResult(HttpCodeEnum.VERIFICATION_FAILED.getCode(),HttpCodeEnum.VERIFICATION_FAILED.getMessage());
    }

    /**
     * 业务逻辑验证未通过
     * @param data
     * @return
     */
    public static ApiResult verificationFailed(Object data) {
        return new ApiResult(HttpCodeEnum.VERIFICATION_FAILED.getCode(),HttpCodeEnum.VERIFICATION_FAILED.getMessage(), data);
    }

    /**
     * 自定义返回
     * @param e
     * @return
     */
    public static ApiResult custom(HttpCodeEnum e) {
        return new ApiResult(e.getCode(),e.getMessage());
    }
    /**
     * 自定义返回
     * @param error
     * @return
     */
    public static ApiResult custom(int code, String error) {
        return new ApiResult(code,error);
    }

    /**
     * 自定义返回
     * @param error
     * @param data
     * @return
     */
    public static ApiResult custom(int code, String error, Object data) {
        return new ApiResult(code,error,data);
    }

}
