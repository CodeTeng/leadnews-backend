package com.lt.feigns;

import com.lt.config.HeimaFeignAutoConfiguration;
import com.lt.feigns.fallback.UserFeignFallback;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.pojo.ApUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 14:06
 */
@FeignClient(value = "leadnews-user", // 调用服务
        fallbackFactory = UserFeignFallback.class, // 服务降级
        configuration = HeimaFeignAutoConfiguration.class) // feign日志配置
public interface UserFeign {
    @GetMapping("/api/v1/user/{id}")
    ResponseResult<ApUser> findUserById(@PathVariable("id") Integer id);
}
