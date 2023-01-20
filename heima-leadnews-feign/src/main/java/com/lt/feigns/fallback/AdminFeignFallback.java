package com.lt.feigns.fallback;

import com.lt.feigns.AdminFeign;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/20 13:55
 */
@Slf4j
@Component
public class AdminFeignFallback implements FallbackFactory<AdminFeign> {
    @Override
    public AdminFeign create(Throwable throwable) {
        throwable.printStackTrace();
        return () -> {
            log.error("AdminFeign sensitives 远程调用出错啦 ~~~ !!!! {} ", throwable.getMessage());
            return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
        };
    }
}
