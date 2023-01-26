package com.lt.article.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.lt.article.mapper.ApArticleMapper;
import com.lt.article.mapper.AuthorMapper;
import com.lt.article.service.GeneratePageService;
import com.lt.file.service.FileStorageService;
import com.lt.model.article.pojo.ApArticle;
import com.lt.model.article.pojo.ApAuthor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 11:39
 */
@Service
@Slf4j
public class GeneratePageServiceImpl implements GeneratePageService {
    @Autowired
    private Configuration configuration;
    @Autowired
    private AuthorMapper authorMapper;
    @Resource(name = "minIOFileStorageService")
    private FileStorageService fileStorageService;
    @Value("${file.minio.prefix}")
    private String prefix;
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public void generateArticlePage(String content, ApArticle apArticle) {
        try {
            // 1. 获取模板文件
            Template template = configuration.getTemplate("article.ftl");
            // 2. 获取数据
            Map<String, Object> map = new HashMap<>();
            map.put("content", JSONArray.parseArray(content));
            map.put("article", apArticle);
            ApAuthor apAuthor = authorMapper.selectById(apArticle.getAuthorId());
            map.put("authorApUserId", apAuthor.getUserId());
            // 3. 转换为 html
            StringWriter out = new StringWriter();
            template.process(map, out);
            // 4. 生成的 html 静态页面上传到 minio 中
            InputStream inputStream = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
            String path = fileStorageService.store(prefix, apArticle.getId() + ".html", "text/html", inputStream);
            // 5. 修改 static_url 字段
            apArticle.setStaticUrl(path);
            apArticleMapper.updateById(apArticle);
            log.info("文章详情静态页生成成功 staticUrl=====> {}", path);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文章详情静态页生成失败=====>articleId : {}    ========> {}", apArticle.getId(), e.getCause());
        }
    }
}
