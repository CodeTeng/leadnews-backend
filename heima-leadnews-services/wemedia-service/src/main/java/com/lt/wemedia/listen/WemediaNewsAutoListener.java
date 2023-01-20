package com.lt.wemedia.listen;

import com.lt.common.constants.wemedia.NewsAutoScanConstants;
import com.lt.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/20 16:03
 */
@Component
@Slf4j
public class WemediaNewsAutoListener {
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    /**
     * queues: 监听指定队列 不会自动创建队列
     * queuesToDeclare：监听指定队列 自动创建队列
     * bindings：监听队列 可以创建 交换机 队列 绑定关系
     */
    @RabbitListener(queuesToDeclare = @Queue(NewsAutoScanConstants.WM_NEWS_AUTO_SCAN_QUEUE))
    public void handleAutoScanMsg(String newsId) {
        // 自动审核
        log.info("接收到 自动审核 消息===> {}", newsId);
        wmNewsAutoScanService.autoScanWmNews(Integer.valueOf(newsId));
    }
}
