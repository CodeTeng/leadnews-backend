package com.lt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.AuthDTO;
import com.lt.model.user.pojo.ApUserRealname;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 11:40
 */
public interface ApUserRealnameService extends IService<ApUserRealname> {
    /**
     * 根据状态查询需要认证相关的用户信息
     */
    ResponseResult getApUserListByStatus(AuthDTO authDTO);

    /**
     * 根据状态进行审核
     *
     * @param authDTO 用户请求信息
     * @param status  用户状态   2-审核失败   9-审核成功
     */
    ResponseResult updateStatusById(AuthDTO authDTO, Short status);
}
