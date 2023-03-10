package com.lt.feigns;

import com.lt.config.HeimaFeignAutoConfiguration;
import com.lt.feigns.fallback.WemediaFeignFallback;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.wemedia.pojo.WmNews;
import com.lt.model.wemedia.pojo.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @description: Wemedia 远程接口
 * @author: ~Teng~
 * @date: 2023/1/15 14:08
 */
@FeignClient(
        value = "leadnews-wemedia", // 微服务名称
        fallbackFactory = WemediaFeignFallback.class,   // 服务降级实现类
        configuration = HeimaFeignAutoConfiguration.class   // feign 的配置---日志级别
)
public interface WemediaFeign {
    @GetMapping("/api/v1/user/findByName/{name}")
    ResponseResult<WmUser> findByName(@PathVariable("name") String name);

    @PostMapping("/api/v1/user/save")
    ResponseResult<WmUser> save(@RequestBody WmUser wmUser);

    @GetMapping("/api/v1/news/one/{id}")
    ResponseResult<WmNews> findWmNewsById(@PathVariable("id") Integer id);

    @PutMapping("/api/v1/news/update")
    ResponseResult updateWmNews(@RequestBody WmNews wmNews);
}
