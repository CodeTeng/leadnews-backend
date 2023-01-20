package com.lt.feigns;

import com.lt.config.HeimaFeignAutoConfiguration;
import com.lt.feigns.fallback.AdminFeignFallback;
import com.lt.model.common.vo.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
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
}
