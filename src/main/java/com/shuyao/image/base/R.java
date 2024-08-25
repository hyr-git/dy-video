package com.shuyao.image.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/* @desc：
 * @CreateName： Major
 * @CreateTime：2020/3/24 13:52
 * @Param:
 * @Return：
 * */
@Data
@ApiModel(value = "全局统一返回结果" )
public class R<T> {

    @ApiModelProperty(value = "是否成功" )
    private Boolean success;

    @ApiModelProperty(value = "返回码" )
    private Integer code;

    @ApiModelProperty(value = "返回的消息" )
    private String message;

    @ApiModelProperty(value = "返回的数据" )
    private T data;

    public R() {
    }

    // ok
    public static R ok() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    // ok
    public static R ok(Object obj) {
        R r = new R();
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        r.setData(obj);
        return r;
    }

    // error
    public static R error() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.UNKNOWN_REASON.getSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return r;
    }

    // error
    public static R error(String message) {
        R r = new R();
        r.setSuccess(ResultCodeEnum.INTENER_ERROR.getSuccess());
        r.setCode(ResultCodeEnum.INTENER_ERROR.getCode());
        r.setMessage(message);
        return r;
    }

    // setResult
    public static R setResult(ResultCodeEnum resultCodeEnum) {
        R r = new R();
        r.setSuccess(resultCodeEnum.getSuccess());
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMessage());
        return r;
    }

    //链式编程
    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R data(T object) {
        this.setData(object);
        return this;
    }


    /**
     * 业务异常
     * @param codeMessage
     */
    public R(CodeMessage codeMessage) {
        handleStatus(codeMessage);
    }

    /**
     * 处理状态
     *
     * @param codeMessage 返回码
     */
    private void handleStatus(CodeMessage codeMessage) {
        this.code = codeMessage.getCode();
        this.message = codeMessage.getMessage();
    }

    public R(CodeMessage codeMessage, T data) {
        handleStatus(codeMessage);
        this.data = data;
    }
}
