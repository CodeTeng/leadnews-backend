package com.lt.feigns.fallback;

import com.lt.feigns.UserFeign;
import com.lt.model.common.enums.AppHttpCodeEnum;
import com.lt.model.common.vo.ResponseResult;
import com.lt.model.user.pojo.ApUser;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 14:06
 */
@Component
@Slf4j
public class UserFeignFallback implements FallbackFactory<UserFeign> {
    @Override
    public UserFeign create(Throwable throwable) {
        throwable.printStackTrace();
        return new UserFeign() {
            @Override
            public ResponseResult<ApUser> findUserById(Integer id) {
                log.error("Feign服务降级触发 远程调用:UserFeign  findUserById 失败,参数:{}", id);
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程服务调用出现异常");
            }
        };
    }
}
