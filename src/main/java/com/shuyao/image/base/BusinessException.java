package com.shuyao.image.base;

import lombok.Getter;

/**
 * 业务异常对象,有业务层抛出
 * @author : tmh
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码对象
     */
    @Getter
    private CodeMessage codeMessage;

    public BusinessException(CodeMessage codeMessage) {
        super(codeMessage.getMessage());
        this.codeMessage = codeMessage;
    }

    public BusinessException(CodeMessage codeMessage, Throwable e) {
        super(codeMessage.getMessage(),e);
        this.codeMessage = codeMessage;
    }

}
