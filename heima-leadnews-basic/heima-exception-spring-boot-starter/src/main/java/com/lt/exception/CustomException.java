package com.lt.exception;

import com.lt.model.common.enums.AppHttpCodeEnum;

/**
 * @description: 自定义异常
 * @author: ~Teng~
 * @date: 2023/1/14 11:23
 */
public class CustomException extends RuntimeException {

    /**
     * 异常处理的枚举
     */
    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum) {
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public CustomException(AppHttpCodeEnum appHttpCodeEnum, String msg) {
        appHttpCodeEnum.setErrorMessage(msg);
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public AppHttpCodeEnum getAppHttpCodeEnum() {
        return appHttpCodeEnum;
    }
}
