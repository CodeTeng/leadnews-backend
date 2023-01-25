package com.lt.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.lt.aliyun.GreenImageScan;
import com.lt.aliyun.GreenTextScan;
import com.lt.common.constants.message.PublishArticleConstants;
import com.lt.exception.CustomException;
import com.lt.feigns.AdminFeign;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.pojo.WmNews;
import com.lt.utils.common.SensitiveWordUtil;
import com.lt.wemedia.mapper.WmNewsMapper;
import com.lt.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/20 13:59
 */
@Service
@Slf4j
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Value("${file.oss.web-site}")
    String webSite;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void autoScanWmNews(Integer id) {
        // 1. 参数校验
        if (id == null || id <= 0) {
            log.error("自动审核文章失败，文章id为空");
            throw new CustomException(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 2. 获取文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            log.error("自动审核文章失败，文章不存在");
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST, "文章不存在");
        }
        String JsonContent = wmNews.getContent();
        if (StringUtils.isBlank(JsonContent)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID, "无法审核，文章内容为空");
        }
        Short status = wmNews.getStatus();
        // 3. 判断文章的状态是否为待审核状态
        if (WmNews.Status.SUBMIT.getCode() != status) {
            log.error("文章未处于待审核状态，无法审核");
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_ALLOW, "无法审核");
        }
        // 4. 进行审核
        // 4.1 抽取出文章中 所有的文本内容 和 所有的图片url集合 Map<String,Object>  content 内容   images List<String>
        Map<String, Object> contentAndImageResult = handleTextAndImages(wmNews);
        String content = (String) contentAndImageResult.get("content");
        // 4.2 DFA 敏感词审核
        boolean isSensitive = handleSensitiveByDFA(content, wmNews);
        if (isSensitive) {
            log.info("文章内容包含敏感，原因为：{}", wmNews.getReason());
            return;
        }
        // 4.3 阿里云文本审核
        isSensitive = handleTextScan(content, wmNews);
        if (isSensitive) {
            log.info("文章内容包含敏感内容，原因为：{}", wmNews.getReason());
            return;
        }
        // 4.4 阿里云图片审核
        List<String> images = (List<String>) contentAndImageResult.get("images");
        if (images != null && images.size() > 0) {
            isSensitive = handleImageScan(images, wmNews);
            if (isSensitive) {
                log.info("文章内容包含敏感，原因为：{}", wmNews.getReason());
                return;
            }
        }
        // 5. 审核通过 更新文章状态
        log.info("审核通过");
        updateWmNews(wmNews, WmNews.Status.SUCCESS.getCode(), "审核通过");
        // 6. 判断发布时间与当前时间关系 通知定时发布文章
        long publishTime = wmNews.getPublishTime().getTime();
        long nowTime = System.currentTimeMillis();
        long remainTime = publishTime - nowTime;
        // 发布文章
        rabbitTemplate.convertAndSend(
                PublishArticleConstants.DELAY_DIRECT_EXCHANGE,
                PublishArticleConstants.PUBLISH_ARTICLE_ROUTE_KEY,
                wmNews.getId(),
                (message) -> {
                    message.getMessageProperties().setHeader("x-delay", remainTime <= 0 ? 0 : remainTime);
                    return message;
                });
        log.info("立即发布文章通知成功发送，文章id : {}", wmNews.getId());
    }

    /**
     * 阿里云图片审核
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews) {
        boolean flag = false;
        try {
            Map map = greenImageScan.imageUrlScan(images);
            String suggestion = (String) map.get("suggestion");
            switch (suggestion) {
                case "block":
                    // 含有违规内容
                    String reason = (String) map.get("reason");
                    // 更新文章状态
                    updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), "图片含有与" + reason + "的内容");
                    flag = true;
                    break;
                case "review":
                    updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), "图片中有不确定内容，转为人工审核");
                    flag = true;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("阿里云图片审核出现异常 , 原因:{}", e.getMessage());
            updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), "阿里云内容服务异常，转为人工审核");
            flag = true;
        }
        return flag;
    }

    /**
     * 阿里云文本审核  block: 状态2    review: 状态3    异常: 状态3
     */
    private boolean handleTextScan(String content, WmNews wmNews) {
        boolean flag = false;
        try {
            Map map = greenTextScan.greenTextScan(content);
            String suggestion = (String) map.get("suggestion");
            switch (suggestion) {
                case "block":
                    // 含有违规内容
                    String reason = (String) map.get("reason");
                    // 更新文章状态
                    updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), "文章含有与" + reason + "的内容");
                    flag = true;
                    break;
                case "review":
                    updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), "文本中有不确定内容，转为人工审核");
                    flag = true;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("阿里云文本审核出现异常 , 原因:{}", e.getMessage());
            updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), "阿里云内容服务异常，转为人工审核");
            flag = true;
        }
        return flag;
    }

    /**
     * 根据 DFA 算法判断文章是否包含敏感词
     *
     * @param content 文章所有文本内容
     */
    private boolean handleSensitiveByDFA(String content, WmNews wmNews) {
        boolean flag = false;
        // 1. 获取数据库中的敏感词
        ResponseResult<List<String>> result = adminFeign.getAllSensitives();
        if (!result.checkCode()) {
            throw new CustomException(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
        List<String> sensitives = result.getData();
        // 2. 将敏感词转为 DFA Map 结构
        SensitiveWordUtil.initMap(sensitives);
        // 3. 检测敏感词
        Map<String, Integer> resultMap = SensitiveWordUtil.matchWords(content);
        if (resultMap.size() > 0) {
            // 3.1 修改文章状态
            updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), "内容包含敏感词：" + resultMap.keySet());
            flag = true;
        }
        return flag;
    }

    /**
     * 修改文章状态
     */
    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    /**
     * 抽取出文章中 所有的文本内容 和 所有的图片url集合
     *
     * @param wmNews 文章实体
     * @return content: 内容 images: List<String>
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        String contentJson = wmNews.getContent();
        List<Map> contentMaps = JSONArray.parseArray(contentJson, Map.class);
        // 1. 抽取文章内容中的文本 以 _hmtt_ 进行分隔
        String content = contentMaps.stream()
                .filter(map -> "text".equals(map.get("type")))
                .map(map -> (String) map.get("value"))
                .distinct()
                .collect(Collectors.joining("_hmtt_"));
        // 1.1 标题也要校验
        content = content + "_hmtt_" + wmNews.getTitle();
        // 2. 抽取文章内容中的所有图片 以 , 分隔
        List<String> imageList = contentMaps.stream()
                .filter(map -> "image".equals(map.get("type")))
                .map(map -> (String) map.get("value"))
                .distinct()
                .collect(Collectors.toList());
        // 2.1 加上封面 图片加上前缀
        String images = wmNews.getImages();
        if (StringUtils.isNotBlank(images)) {
            List<String> urls = Arrays.stream(images.split(","))
                    .map(url -> webSite + url)
                    .collect(Collectors.toList());
            imageList.addAll(urls);
        }
        // 2.2 最终去重得到文章中所有图片
        imageList = imageList.stream().distinct().collect(Collectors.toList());
        // 3. 返回数据
        Map<String, Object> map = new HashMap<>(2);
        map.put("content", content);
        map.put("images", imageList);
        return map;
    }
}
