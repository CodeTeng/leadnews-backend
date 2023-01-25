package com.lt.wemedia.config;

import com.lt.common.constants.message.PublishArticleConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 声明定时发布文章所需的 所有交换机  队列 及 绑定关系
 * @author: ~Teng~
 * @date: 2023/1/25 17:59
 */
@Configuration
public class DeclarePubArticleRabbitConfig {
    /**
     * 延迟交换机
     */
    @Bean
    public DirectExchange delayExchange() {
        return ExchangeBuilder
                .directExchange(PublishArticleConstants.DELAY_DIRECT_EXCHANGE)
                .delayed()
                .durable(true)
                .build();
    }

    /**
     * 声明发布文章队列
     */
    @Bean
    public Queue publishArticleQueue() {
        return new Queue(PublishArticleConstants.PUBLISH_ARTICLE_QUEUE, true);
    }

    /**
     * 绑定 延迟交换机 + 发布文章队列
     */
    @Bean
    public Binding bindingPublishArticleQueue() {
        return BindingBuilder.bind(publishArticleQueue()).to(delayExchange()).with(PublishArticleConstants.PUBLISH_ARTICLE_ROUTE_KEY);
    }
}
