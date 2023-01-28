package com.lt.exception;

import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 全局异常处理器
 * @author: ~Teng~
 * @date: 2023/1/14 11:17
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult globalException(Exception e) {
        e.printStackTrace();
        log.error("捕获全局异常: {}", e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(CustomException.class)
    public ResponseResult cutsException(CustomException e) {
        e.printStackTrace();
        log.error("捕获自定义异常: {}", e.getMessage());
        return ResponseResult.errorResult(e.getAppHttpCodeEnum());
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult handleValidationException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException ex: {}", ex.getMessage());
        ex.printStackTrace();
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, ex.getBindingResult().getFieldError().getDefaultMessage());
    }
}
