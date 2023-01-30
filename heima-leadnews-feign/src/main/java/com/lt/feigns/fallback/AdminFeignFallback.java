package com.lt.feigns.fallback;

import com.lt.feigns.AdminFeign;
import com.lt.model.admin.pojo.AdChannel;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


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
        return new AdminFeign() {
            @Override
            public ResponseResult<List<String>> getAllSensitives() {
                log.error("AdminFeign sensitives 远程调用出错啦 ~~~ !!!! {} ", throwable.getMessage());
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
            }

            @Override
            public ResponseResult findOne(Integer id) {
                log.info("参数: {}", id);
                log.error("AdminFeign findOne 远程调用出错啦 ~~~ !!!! {} ", throwable.getMessage());
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
            }

            @Override
            public ResponseResult<List<AdChannel>> selectChannels() {
                log.error("AdminFeign selectChannels 远程调用出错啦 ~~~ !!!! {} ", throwable.getMessage());
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
            }
        };
    }
}
