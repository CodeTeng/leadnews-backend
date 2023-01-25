package com.lt.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.article.mapper.ApArticleContentMapper;
import com.lt.article.service.ApArticleContentService;
import com.lt.model.article.pojo.ApArticleContent;
import org.springframework.stereotype.Service;

@Service
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent> implements ApArticleContentService {

}