package com.lt.feigns;

import com.lt.config.HeimaFeignAutoConfiguration;
import com.lt.feigns.fallback.AdminFeignFallback;
import com.lt.model.admin.pojo.AdChannel;
import com.lt.model.common.vo.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/20 13:53
 */
@FeignClient(value = "leadnews-admin",
        configuration = HeimaFeignAutoConfiguration.class,
        fallbackFactory = AdminFeignFallback.class
)
public interface AdminFeign {
    @PostMapping("/api/v1/sensitive/sensitives")
    ResponseResult<List<String>> getAllSensitives();

    @GetMapping("/api/v1/channel/one/{id}")
    ResponseResult<AdChannel> findOne(@PathVariable Integer id);
}
