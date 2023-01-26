package com.lt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.dto.ApUserLoginDTO;
import com.lt.model.user.pojo.ApUser;
import com.lt.user.mapper.ApUserMapper;
import com.lt.user.service.ApUserLoginService;
import com.lt.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 17:41
 */
@Service
public class ApUserLoginServiceImpl implements ApUserLoginService {
    @Autowired
    private ApUserMapper apUserMapper;


    @Override
    public ResponseResult login(ApUserLoginDTO apUserLoginDTO) {
        // 1. 判断登录态
        String phone = apUserLoginDTO.getPhone();
        String password = apUserLoginDTO.getPassword();
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(password)) {
            // 1.1 登录
            ApUser apUser = apUserMapper.selectOne(new LambdaQueryWrapper<ApUser>().eq(ApUser::getPhone, phone));
            if (apUser == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "请检查手机号");
            }
            String dbPassword = apUser.getPassword();
            String encryptedPassword = DigestUtils.md5DigestAsHex((password + apUser.getSalt()).getBytes(StandardCharsets.UTF_8));
            if (!dbPassword.equals(encryptedPassword)) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "手机号或密码错误");
            }
            // 返回 token
            apUser.setPassword("");
            apUser.setSalt("");
            Map<String, Object> map = new HashMap<>(2);
            map.put("token", AppJwtUtil.getToken(apUser.getId()));
            map.put("user", apUser);
            return ResponseResult.okResult(map);
        } else {
            // 1.2 未登录查看
            Integer equipmentId = apUserLoginDTO.getEquipmentId();
            if (equipmentId == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            // 返回默认 token
            Map<String, Object> map = new HashMap<>(2);
            // 通过设备ID登录的 userId 存0
            map.put("token", AppJwtUtil.getToken(0));
            return ResponseResult.okResult(map);
        }
    }
}
