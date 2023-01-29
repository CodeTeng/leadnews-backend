package com.lt.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.model.user.pojo.ApUser;
import com.lt.user.mapper.ApUserMapper;
import com.lt.user.service.ApUserService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 14:05
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
}
