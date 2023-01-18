package com.lt.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.dto.LoginWmUserDTO;
import com.lt.model.wemedia.pojo.WmUser;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 13:56
 */
public interface WmUserService extends IService<WmUser> {
    /**
     * 自媒体端登录
     *
     * @param loginWmUserDTO 登录请求实体
     */
    ResponseResult login(LoginWmUserDTO loginWmUserDTO);
}
