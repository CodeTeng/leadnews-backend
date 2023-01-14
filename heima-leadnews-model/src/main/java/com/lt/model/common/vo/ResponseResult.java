package com.lt.model.common.vo;

import com.alibaba.fastjson.JSON;
import com.lt.model.common.enums.AppHttpCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 通用返回结果类
 * @author: ~Teng~
 * @date: 2023/1/13 17:16
 */
@Data
public class ResponseResult<T> implements Serializable {
    /**
     * 状态码
     */
    private Integer code = 200;
    /**
     * 提示信息
     */
    private String errorMessage;
    /**
     * 数据
     */
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String errorMessage, T data) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public ResponseResult(Integer code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }

    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }

    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getErrorMessage());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult okResult() {
        return okResult(null);
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getErrorMessage());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums, String msg) {
        return setAppHttpCodeEnum(enums, msg);
    }

    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getErrorMessage());
    }

    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String msg) {
        return okResult(enums.getCode(), msg);
    }

    public ResponseResult<?> error(Integer code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String errorMessage) {
        this.code = code;
        this.data = data;
        this.errorMessage = errorMessage;
        return this;
    }

    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public boolean checkCode() {
        if (this.getCode().intValue() != 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("成功的响应:");
        System.out.println(JSON.toJSONString(ResponseResult.okResult()));
        System.out.println("成功带返回值的响应:");
        Map map = new HashMap<>();
        map.put("phone", "13010102121");
        System.out.println(JSON.toJSONString(ResponseResult.okResult(map)));
        System.out.println("错误的响应:");
        System.out.println(JSON.toJSONString(ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST)));
        new PageResponseResult(1, 10, 29L, new ArrayList<>());
    }
}
