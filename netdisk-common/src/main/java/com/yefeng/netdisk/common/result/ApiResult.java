package com.yefeng.netdisk.common.result;

import java.io.Serializable;


/**
 * 操作结果集封装
 * @author zealon
 */
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)

public class ApiResult<T>  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ApiResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult() {

    }

    public ApiResult(T data) {
        this.data = data;
        this.code=HttpCodeEnum.OK.getCode();
        this.msg=HttpCodeEnum.OK.getMessage();
    }
    public ApiResult(HttpCodeEnum codeEnum,T data) {
        this.code=codeEnum.getCode();
        this.msg=codeEnum.getMessage();
        this.data = data;
    }
    public ApiResult(HttpCodeEnum codeEnum) {
        this.code=codeEnum.getCode();
        this.msg=codeEnum.getMessage();

    }


    /**
     * 得到json字符串
     *
     * @return {@link String}
     */
    private String getJsonString() {
        String res = "{";
        if (code != null) {
            res += "\"errCode\":" + code;
        }
        if (msg != null) {
            res += ",\"errMsg\":\"" + msg + "\"";
        }
        if (data != null) {
            res += ",\"data\":" + data;
        }
        res += "}";
        return res;
    }
//    public JSONObject toJsonObject() {
//        JSONObject jsonObject = JSONUtil.parseObj(getJsonString());
//
//        return jsonObject;
//    }
//    @Override
//    public String toString() {
//
//        return getJsonString();
//    }
    /**
     * 构建消息内容
     * @param msg
     * @return
     */
    public ApiResult buildMessage(String msg){
        this.setMsg(msg);
        return this;
    }

    /**
     * 构建消息data的值，key默认为data
     * @param obj data值
     * @return
     */
    public ApiResult buildData(T obj){
        this.setData(obj);
        return this;

    }

    public int getCode() {
        return code;
    }

    public ApiResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ApiResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResult setData(T data) {
        this.data = data;
        return this;
    }
}
