package com.lt.feigns.fallback;

import com.lt.feigns.BehaviorFeign;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 13:29
 */
@Component
@Slf4j
public class BehaviorFeignFallback implements FallbackFactory<BehaviorFeign> {
    @Override
    public BehaviorFeign create(Throwable throwable) {
        throwable.printStackTrace();
        return new BehaviorFeign() {
            @Override
            public ResponseResult<ApBehaviorEntry> findByUserIdOrEquipmentId(Integer userId, Integer equipmentId) {
                log.error("Feign服务降级触发 远程调用:BehaviorFeign  findByUserIdOrEquipmentId 失败,参数:userId={} equipmentId={}", userId, equipmentId);
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程服务调用出现异常");
            }
        };
    }
}
