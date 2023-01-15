package com.lt.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.model.wemedia.pojo.WmUser;
import com.lt.wemedia.mapper.WmUserMapper;
import com.lt.wemedia.service.WmUserService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/15 13:56
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {
}
