package com.lt.wemedia.service;

/**
 * @description: 自动审核 Service
 * @author: ~Teng~
 * @date: 2023/1/20 13:58
 */
public interface WmNewsAutoScanService {
    /**
     * 自媒体文章审核
     *
     * @param id 自媒体文章id
     */
    void autoScanWmNews(Integer id);
}
