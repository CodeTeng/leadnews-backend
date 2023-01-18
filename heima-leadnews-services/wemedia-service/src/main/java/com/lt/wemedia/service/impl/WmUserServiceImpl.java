package com.lt.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.common.constants.admin.AdminConstants;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.dto.LoginWmUserDTO;
import com.lt.model.wemedia.pojo.WmUser;
import com.lt.model.wemedia.vo.WmUserVO;
import com.lt.utils.common.AppJwtUtil;
import com.lt.wemedia.mapper.WmUserMapper;
import com.lt.wemedia.service.WmUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 13:56
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

    @Override
    public ResponseResult login(LoginWmUserDTO loginWmUserDTO) {
        String name = loginWmUserDTO.getName();
        String password = loginWmUserDTO.getPassword();
        // 查询自媒体用户
        WmUser wmUser = this.getOne(new LambdaQueryWrapper<WmUser>().eq(WmUser::getName, name));
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该自媒体用户不存在");
        }
        if (wmUser.getStatus() != 9) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_STATUS_ERROR);
        }
        String dbPassword = wmUser.getPassword();
        String encryptedPassword = DigestUtils.md5DigestAsHex((password + wmUser.getSalt()).getBytes(StandardCharsets.UTF_8));
        if (!dbPassword.equals(encryptedPassword)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 修改登录时间
        wmUser.setLoginTime(new Date());
        this.updateById(wmUser);
        // 返回 jwt 给用户
        String token = AppJwtUtil.getToken(wmUser.getId());
        WmUserVO wmUserVO = new WmUserVO();
        BeanUtils.copyProperties(wmUser, wmUserVO);
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", token);
        map.put("user", wmUserVO);
        return ResponseResult.okResult(map);
    }
}
