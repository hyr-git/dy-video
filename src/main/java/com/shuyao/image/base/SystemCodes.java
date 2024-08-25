package com.shuyao.image.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统编码
 */
@Getter
@AllArgsConstructor
public enum SystemCodes implements CodeMessage {

    /**
     * 200-成功
     */
    SUCCESS(200, "成功", "success"),
    /**
     * 500-服务器繁忙，请稍后重试
     */
    SYSTEM_ERROR(500, "服务器繁忙，请稍后重试", "system_error"),
    /**
     * 400-参数非法
     */
    PARAM_IS_ILLEGAL(400, "参数非法: %s", "param_is_illegal"),
    /**
     * 401-系统暂无权限，请检查后重试
     */
    PERMISSIONS_ILLEGAL(401, "系统暂无权限，请检查后重试", "permissions_illegal"),
    /**
     * 402-请求过快,请稍后重试
     */
    REQUEST_FAST(402, "请求过快,请稍后重试", "request_fast"),
    /**
     * 404-请求地址不正确
     */
    URL_NOT_FOUND(404, "请求地址不正确: %s", "url_not_found"),
    /**
     * 4001-参数格式错误,请检查参数格式
     */
    PARAM_FORMAT_ERROR(4001, "参数格式错误,请检查参数格式", "param_format_error"),
    /**
     * 4002-上传文件已经超过限制
     */
    UPLOAD_SIZEOVER_ERROR(4002, "上传文件已经超过%s大小限制，请重新上传", "upload_sizeover_error"),
    /**
     * 4003-请求缺少签名值
     */
    MISS_SIGN(4003, "请求缺少签名值", "miss_sign"),
    /**
     * 4004-请求的签名值不正确
     */
    ERROR_SIGN_TOKEN(4004, "请求的签名值不正确", "error_sign_token"),
    /**
     * 5001-外部调用异常
     */
    EXTERNAL_CALL_ERROR(5001, "外部调用异常", "external_call_error"),;

    /**
     * 返回码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应消息
     */
    private String description;

}
