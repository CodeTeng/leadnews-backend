package com.lt.user.service;

import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.ApUserLoginDTO;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 17:40
 */
public interface ApUserLoginService {
    /**
     * app端登录
     */
    ResponseResult login(ApUserLoginDTO apUserLoginDTO);
}
