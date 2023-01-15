package com.lt.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.admin.mapper.AdUserMapper;
import com.lt.admin.service.AdUserService;
import com.lt.model.admin.dto.aduser.LoginAdUserDTO;
import com.lt.model.admin.pojo.AdUser;
import com.lt.model.admin.vo.aduser.AdUserVO;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.utils.common.AppJwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 20:41
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    @Override
    public ResponseResult login(LoginAdUserDTO loginAdUserDTO) {
        // 1. 检查用户是否存在
        String name = loginAdUserDTO.getName();
        AdUser adUser = this.getOne(new LambdaQueryWrapper<AdUser>().eq(AdUser::getName, name));
        if (adUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        if (adUser.getStatus() != 9) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_STATUS_ERROR);
        }
        // 2. 验证密码 encryption
        String originPassword = loginAdUserDTO.getPassword();
        String dbPassword = adUser.getPassword();
        String salt = adUser.getSalt();
        String encryptionPassword = DigestUtils.md5DigestAsHex((originPassword + salt).getBytes(StandardCharsets.UTF_8));
        if (!dbPassword.equals(encryptionPassword)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 3. 修改登录时间 并且生成 token
        adUser.setLoginTime(new Date());
        this.updateById(adUser);
        String token = AppJwtUtil.getToken(adUser.getId());
        // 4. 封装返回信息
        AdUserVO adUserVO = new AdUserVO();
        BeanUtils.copyProperties(adUser, adUserVO);
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", token);
        map.put("user", adUserVO);
        return ResponseResult.okResult(map);
    }
}
