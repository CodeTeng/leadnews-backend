package com.lt.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.article.mapper.ApArticleConfigMapper;
import com.lt.article.service.ApArticleConfigService;
import com.lt.model.article.pojo.ApArticleConfig;
import org.springframework.stereotype.Service;

@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {
}