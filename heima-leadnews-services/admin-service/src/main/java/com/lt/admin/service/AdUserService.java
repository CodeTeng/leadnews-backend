package com.lt.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.admin.dto.aduser.LoginAdUserDTO;
import com.lt.model.admin.pojo.AdUser;
import com.lt.model.common.vo.ResponseResult;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 20:40
 */
public interface AdUserService extends IService<AdUser> {
    /**
     * 用户登录
     *
     * @param loginAdUserDTO 用户登录请求体
     */
    ResponseResult login(LoginAdUserDTO loginAdUserDTO);
}
