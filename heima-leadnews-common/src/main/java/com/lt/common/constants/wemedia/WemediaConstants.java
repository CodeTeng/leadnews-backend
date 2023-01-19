package com.lt.common.constants.wemedia;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 23:39
 */
public interface WemediaConstants {
    /**
     * 是否收藏
     */
    Short COLLECT_MATERIAL = 1; //收藏
    Short CANCEL_COLLECT_MATERIAL = 0; //未收藏
    /**
     * 文章类型
     */
    String WM_NEWS_TYPE_IMAGE = "image";
    /**
     * 文章状态
     */
    Short WM_NEWS_DRAFT_STATUS = 0; //草稿
    Short WM_NEWS_SUMMIT_STATUS = 1; //提交
    Short WM_NEWS_AUTHED_STATUS = 8; //审核通过
    Short WM_NEWS_PUBLISH_STATUS = 9; //已发布
    /**
     * 文章封面选图
     */
    Short WM_NEWS_NONE_IMAGE = 0; //无图
    Short WM_NEWS_SINGLE_IMAGE = 1; //单图
    Short WM_NEWS_MANY_IMAGE = 3; //多图
    Short WM_NEWS_TYPE_AUTO = -1; //图文类型自动
    /**
     * 文章图片引用
     */
    Short WM_CONTENT_REFERENCE = 0;
    Short WM_IMAGE_REFERENCE = 1;
    /**
     * 文章上下架状态
     */
    Short WM_NEWS_UP = 1; // 上架
    Short WM_NEWS_DOWN = 0; // 下架
}
