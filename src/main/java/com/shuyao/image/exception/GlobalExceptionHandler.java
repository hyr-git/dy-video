package com.shuyao.image.exception;

import com.shuyao.image.base.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERRPR);
    }


    /**
     * 400错误
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public R processMethod(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return R.error().message(e.getMessage()).code(HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public R processMethod(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return R.error().message(e.getMessage()).code(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 参数处理异常
     *
     * @param exption MethodArgumentNotValidException 参数校验异常类
     * @return ResponseResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public R globalParamException(MethodArgumentNotValidException exption) {
        List<ObjectError> allErrors = exption.getBindingResult().getAllErrors();
        List<String> errorMsgs = allErrors.stream().filter(item -> StringUtils.isNotBlank(item.getDefaultMessage())).map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        exption.printStackTrace();
        log.error(exption.getMessage(), exption);
        R r = new R<>(CodeMessage.formatMessage(SystemCodes.PARAM_IS_ILLEGAL, StringUtils.join(errorMsgs, ",")));
        r.setSuccess(false);
        return r;
    }

    /**
     * 业务异常处理
     *
     * @param exption businessException
     * @return ResponseResult
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public R globalBusinessException(BusinessException exption, HttpServletResponse response) {
        CodeMessage codeMessage = exption.getCodeMessage();
        response.setStatus(HttpStatus.OK.value());
        log.error(codeMessage.getCode() + "-" + exption.getMessage(), exption);
        R r = new R(codeMessage);
        r.setCode(codeMessage.getCode());
        r.setSuccess(false);
        return r;
    }

    /**
     * 兜底异常处理，返回500错误
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public R globalException(Exception exption) {
        SystemCodes systemError = SystemCodes.SYSTEM_ERROR;
        log.error(exption.getMessage(), exption);
        R r = new R<>(systemError, null);
        r.setSuccess(false);
        return r;
    }

}