package com.lt.model.common.enums;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/13 17:17
 */
public enum AppHttpCodeEnum {
    // 成功段0
    SUCCESS(0, "操作成功"),
    // 登录段1~50
    NEED_LOGIN(1, "需要登录后操作"),
    LOGIN_PASSWORD_ERROR(2, "用户名或密码错误"),
    LOGIN_STATUS_ERROR(3, "用户状态异常,请联系管理员"),

    // TOKEN50~100
    TOKEN_INVALID(50, "无效的TOKEN"),
    TOKEN_EXPIRE(51, "TOKEN已过期"),
    TOKEN_REQUIRE(52, "TOKEN是必须的"),
    // SIGN验签 100~120
    SIGN_INVALID(100, "无效的SIGN"),
    SIG_TIMEOUT(101, "SIGN已过期"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500, "缺少参数"),
    PARAM_INVALID(501, "无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502, "图片格式有误"),
    SERVER_ERROR(503, "服务器内部错误"),
    REMOTE_SERVER_ERROR(504, "远程调用出错"),
    // 数据错误 1000~2000
    DATA_EXIST(1000, "数据已经存在"),
    AP_USER_DATA_NOT_EXIST(1001, "ApUser数据不存在"),
    DATA_NOT_EXIST(1002, "数据不存在"),
    DATA_NOT_ALLOW(1003, "数据不允许此操作"),
    WM_USER_NOT_EXIST(1004, "自媒体用户不存在"),
    // 数据错误 3000~3500
    NO_OPERATOR_AUTH(3000, "无权限操作"),
    NEED_ADMIND(3001, "需要管理员权限"),

    // admin code 4000-4999
    ADMIN_CHANNEL_NOT_EXIST(4000, "频道不存在"),
    ADMIN_CHANNEL_EXIST(4001, "频道已存在"),
    ADMIN_CHANNEL_STATUS_TRUE(4002, "频道有效，无法删除"),
    ADMIN_SENSITIVE_EXIST(4003, "敏感词已存在"),
    ADMIN_SENSITIVE_NOT_EXIST(4004, "敏感词不存在"),
    ADMIN_AD_USER_NOT_EXIST(4005, "AdUser用户不存在"),
    ADMIN_AD_USER_EXIST(4006, "AdUser用户存在"),

    // operation code 5000-5999
    OPERATION_CHANNEL_INSERT_ERROR(5000, "添加失败"),
    OPERATION_CHANNEL_INSERT_BATCH_ERROR(5001, "批量添加失败"),
    OPERATION_CHANNEL_UPDATE_ERROR(5002, "更新失败"),
    OPERATION_CHANNEL_DELETE_ERROR(5003, "删除失败"),
    OPERATION_CHANNEL_DELETE_BATCH_ERROR(5004, "批量删除失败");

    int code;
    String errorMessage;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
