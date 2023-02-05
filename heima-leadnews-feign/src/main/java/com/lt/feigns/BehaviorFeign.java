package com.lt.feigns;

import com.lt.config.HeimaFeignAutoConfiguration;
import com.lt.feigns.fallback.BehaviorFeignFallback;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.common.vo.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 13:29
 */
@FeignClient(value = "leadnews-behavior",
        fallbackFactory = BehaviorFeignFallback.class,
        configuration = HeimaFeignAutoConfiguration.class)
public interface BehaviorFeign {
    @GetMapping("/api/v1/behavior_entry/one")
    ResponseResult<ApBehaviorEntry> findByUserIdOrEquipmentId(@RequestParam("userId") Integer userId,
                                                              @RequestParam("equipmentId") Integer equipmentId);
}
