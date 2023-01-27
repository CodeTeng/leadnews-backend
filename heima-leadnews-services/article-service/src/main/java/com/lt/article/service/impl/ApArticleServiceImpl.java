package com.lt.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.article.mapper.ApArticleConfigMapper;
import com.lt.article.mapper.ApArticleContentMapper;
import com.lt.article.mapper.ApArticleMapper;
import com.lt.article.mapper.AuthorMapper;
import com.lt.article.service.ApArticleService;
import com.lt.article.service.GeneratePageService;
import com.lt.common.constants.article.ArticleConstants;
import com.lt.exception.CustomException;
import com.lt.feigns.AdminFeign;
import com.lt.feigns.WemediaFeign;
import com.lt.model.admin.pojo.AdChannel;
import com.lt.model.article.dto.ArticleHomeDTO;
import com.lt.model.article.pojo.ApArticle;
import com.lt.model.article.pojo.ApArticleConfig;
import com.lt.model.article.pojo.ApArticleContent;
import com.lt.model.article.pojo.ApAuthor;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.pojo.WmNews;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {
    @Autowired
    private WemediaFeign wemediaFeign;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private GeneratePageService generatePageService;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Value("${file.oss.web-site}")
    private String webSite;
    @Value("${file.minio.readPath}")
    private String readPath;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void publishArticle(Integer newsId) {
        if (newsId == null || newsId <= 0) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 1. 根据文章 id 查询 wmNews 内容信息 并判断状态
        WmNews wmNews = getWmNews(newsId);
        // 2. 封装 apArticle 信息
        ApArticle apArticle = getApArticle(wmNews);
        // 3. 保存或修改文章信息
        saveOrUpdateArticle(apArticle);
        // 4. 保存 apConfig 和 apContent 信息
        saveConfigAndContent(wmNews, apArticle);
        // 5. 利用 freemarker 页面静态化
        generatePageService.generateArticlePage(wmNews.getContent(), apArticle);
        // 6. 更新 wmNews 状态，改为已发布，并设置 articleID
        updateWmNews(wmNews, apArticle);
        // 7. todo 通知 es 索引库添加索引
    }

    /**
     * 封装 ApArticle
     */
    private ApArticle getApArticle(WmNews wmNews) {
        // 1. 封装 ApArticle
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(wmNews, apArticle);
        apArticle.setLayout(wmNews.getType());
        apArticle.setFlag((byte) 0);
        apArticle.setId(wmNews.getArticleId());
        // 2. 查询频道信息
        ResponseResult<AdChannel> channelResponseResult = adminFeign.findOne(wmNews.getChannelId());
        if (!channelResponseResult.checkCode()) {
            log.error("文章发布失败 远程调用查询频道出现异常， 不予发布 , 文章id : {}  频道id : {}", wmNews.getId(), wmNews.getChannelId());
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程调用查询频道出现异常");
        }
        AdChannel adChannel = channelResponseResult.getData();
        if (adChannel == null) {
            log.error("文章发布失败 未查询到相关频道信息， 不予发布 , 文章id : {}  频道id : {}", wmNews.getId(), wmNews.getChannelId());
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST, "未查询到相关频道信息");
        }
        apArticle.setChannelName(adChannel.getName());
        // 3. 查询作者信息
        ApAuthor apAuthor = authorMapper.selectOne(new LambdaQueryWrapper<ApAuthor>().eq(ApAuthor::getWmUserId, wmNews.getUserId()));
        if (apAuthor == null) {
            log.error("文章发布失败 未查询到相关作者信息， 不予发布 , 文章id : {}  自媒体用户id : {}", wmNews.getId(), wmNews.getUserId());
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST, "根据自媒体用户，查询关联作者信息失败");
        }
        apArticle.setAuthorId(Long.valueOf(apAuthor.getId()));
        apArticle.setAuthorName(apAuthor.getName());
        return apArticle;
    }

    /**
     * 根据文章 id 查询 wmNews 内容信息 并判断状态
     */
    private WmNews getWmNews(Integer newsId) {
        ResponseResult<WmNews> wmNewsResponseResult = wemediaFeign.findWmNewsById(newsId);
        if (!wmNewsResponseResult.checkCode()) {
            log.error("文章发布失败 远程调用自媒体文章接口失败  文章id: {}", newsId);
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程调用自媒体文章接口失败");
        }
        WmNews wmNews = wmNewsResponseResult.getData();
        if (wmNews == null) {
            log.error("文章发布失败 未获取到自媒体文章信息  文章id: {}", newsId);
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST, "未查询到自媒体文章");
        }
        Short status = wmNews.getStatus();
        if (status != WmNews.Status.ADMIN_SUCCESS.getCode() && status != WmNews.Status.SUCCESS.getCode()) {
            log.error("文章发布失败 文章状态不为 4 或 8， 不予发布 , 文章id : {}", newsId);
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_ALLOW, "自媒体文章状态错误");
        }
        return wmNews;
    }

    /**
     * 修改自媒体文章
     */
    private void updateWmNews(WmNews wmNews, ApArticle apArticle) {
        wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
        wmNews.setArticleId(apArticle.getId());
        ResponseResult responseResult = wemediaFeign.updateWmNews(wmNews);
        if (!responseResult.checkCode()) {
            log.error("文章发布失败 远程调用修改文章接口失败， 不予发布 , 文章id : {} ", wmNews.getId());
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程调用修改文章接口失败");
        }
    }

    /**
     * 保存 apConfig 和 apContent 信息
     */
    private void saveConfigAndContent(WmNews wmNews, ApArticle apArticle) {
        // 添加配置信息
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsForward(true);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsDelete(false);
        apArticleConfigMapper.insert(apArticleConfig);
        // 添加文章详情
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(wmNews.getContent());
        apArticleContentMapper.insert(apArticleContent);
    }

    /**
     * 保存或修改文章信息
     *
     * @param apArticle apArticle
     */
    private void saveOrUpdateArticle(ApArticle apArticle) {
        if (apArticle.getId() == null) {
            // 保存
            apArticle.setCollection(0);
            apArticle.setLikes(0);
            apArticle.setComment(0);
            apArticle.setViews(0);
            this.save(apArticle);
        } else {
            // 修改
            // 删除之前关联的配置信息 内容信息
            ApArticle article = this.getById(apArticle.getId());
            if (article == null) {
                throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST, "关联的文章不存在");
            }
            apArticleConfigMapper.delete(new LambdaQueryWrapper<ApArticleConfig>().eq(ApArticleConfig::getArticleId, article.getId()));
            apArticleContentMapper.delete(new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, article.getId()));
            // 更新文章
            this.updateById(apArticle);
        }
    }

    @Override
    public ResponseResult load(Short loadtype, ArticleHomeDTO dto) {
        // 1. 参数校验
        // 分页
        Integer size = dto.getSize();
        if (size == null || size <= 0) {
            dto.setSize(10);
        }
        // 频道
        String tag = dto.getTag();
        if (StringUtils.isBlank(tag)) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        // 时间
        Date minBehotTime = dto.getMinBehotTime();
        if (minBehotTime == null) {
            dto.setMinBehotTime(new Date());
        }
        Date maxBehotTime = dto.getMaxBehotTime();
        if (maxBehotTime == null) {
            dto.setMaxBehotTime(new Date());
        }
        // 类型
        if (!loadtype.equals(ArticleConstants.LOADTYPE_LOAD_MORE) &&
                !loadtype.equals(ArticleConstants.LOADTYPE_LOAD_NEW)) {
            loadtype = ArticleConstants.LOADTYPE_LOAD_MORE;
        }
        // 2. 查询 mapper
        List<ApArticle> apArticles = apArticleMapper.loadArticleList(dto, loadtype);
        // 添加静态页面访问前缀 和 阿里云访问前缀
        apArticles = apArticles.stream().peek(apArticle -> {
            String images = apArticle.getImages();
            apArticle.setStaticUrl(readPath + apArticle.getStaticUrl());
            if (StringUtils.isNotBlank(images)) {
                images = Arrays.stream(images.split(","))
                        .map(url -> webSite + url)
                        .collect(Collectors.joining(","));
                apArticle.setImages(images);
            }
        }).collect(Collectors.toList());
        return ResponseResult.okResult(apArticles);
    }
}