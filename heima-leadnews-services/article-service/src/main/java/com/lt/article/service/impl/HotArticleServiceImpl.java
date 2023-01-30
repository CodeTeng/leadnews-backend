package com.lt.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.lt.article.mapper.ApArticleMapper;
import com.lt.article.service.HotArticleService;
import com.lt.common.constants.article.ArticleConstants;
import com.lt.exception.CustomException;
import com.lt.feigns.AdminFeign;
import com.lt.model.admin.pojo.AdChannel;
import com.lt.model.article.pojo.ApArticle;
import com.lt.model.article.vo.HotArticleVo;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 13:51
 */
@Service
@Slf4j
public class HotArticleServiceImpl implements HotArticleService {
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void computeHotArticle() {
        // 1. 查询前5天的文章
        // 1.1 计算前5天的时间
        String beginTime = LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 1.2 查询文章
        List<ApArticle> apArticleList = apArticleMapper.selectArticleByDate(beginTime);
        if (CollectionUtils.isEmpty(apArticleList)) {
            log.error("项目太冷清了，近日暂无文章");
            return;
        }
        // 2. 将文章转为热点VO 并计算每个文章的分值
        List<HotArticleVo> hotArticleVoList = computeArticleScore(apArticleList);
        // 3. 按照频道找出分值较高的30条，存入缓存
        // 3.1 查询所有频道列表
        ResponseResult<List<AdChannel>> responseResult = adminFeign.selectChannels();
        if (!responseResult.checkCode()) {
            log.error(AppHttpCodeEnum.REMOTE_SERVER_ERROR.getErrorMessage());
            return;
        }
        List<AdChannel> channelList = responseResult.getData();
        // 3.2 按照频道存入缓存
        channelList.forEach(adChannel -> {
            List<HotArticleVo> hotArticleVos = hotArticleVoList.stream().filter(hotArticleVo -> hotArticleVo.getChannelId().equals(adChannel.getId())).collect(Collectors.toList());
            sortAndCache(hotArticleVos, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + adChannel.getId());
        });
        // 4. 推荐频道 直接存入缓存
        sortAndCache(hotArticleVoList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
    }

    /**
     * 将热点文章按照分值降序排序 30条 存入缓存中
     *
     * @param hotArticleVoList 热点文章集合
     * @param cacheKey         缓存key
     */
    private void sortAndCache(List<HotArticleVo> hotArticleVoList, String cacheKey) {
        hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore)).limit(30).collect(Collectors.toList());
        stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(hotArticleVoList));
    }

    /**
     * 将文章转为热点VO 并计算每篇文章的分值
     */
    private List<HotArticleVo> computeArticleScore(List<ApArticle> apArticleList) {
        return apArticleList.stream().map(apArticle -> {
            HotArticleVo hotArticleVo = new HotArticleVo();
            BeanUtils.copyProperties(apArticle, hotArticleVo);
            // 计算分值
            Integer score = computeScore(apArticle);
            hotArticleVo.setScore(score);
            return hotArticleVo;
        }).collect(Collectors.toList());
    }

    /**
     * 计算每篇文章的分值
     */
    private Integer computeScore(ApArticle apArticle) {
        Integer score = 0;
        Integer likes = apArticle.getLikes();
        if (likes != null) {
            score += likes * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        Integer collection = apArticle.getCollection();
        if (collection != null) {
            score += collection * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        Integer comment = apArticle.getComment();
        if (comment != null) {
            score += comment * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        Integer views = apArticle.getViews();
        if (views != null) {
            score += views * ArticleConstants.HOT_ARTICLE_VIEW_WEIGHT;
        }
        return score;
    }
}
