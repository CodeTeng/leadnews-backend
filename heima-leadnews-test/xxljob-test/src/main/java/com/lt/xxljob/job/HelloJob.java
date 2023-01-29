package com.lt.xxljob.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/29 21:39
 */
@Component
public class HelloJob {
    @Value("${server.port}")
    private String appPort;

    @XxlJob("helloJob")
    public ReturnT<String> hello(String param) {
        System.out.println(param + " helloJob：" + LocalDateTime.now() + ",端口号" + appPort);
        return ReturnT.SUCCESS;
    }

    /**
     * 2、分片广播任务
     */
    @XxlJob("shardingJobHandler")
    public ReturnT<String> shardingJobHandler(String param) {
        // 设置分片参数
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        XxlJobLogger.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.getIndex(), shardingVO.getTotal());
        List<Integer> list = getList();
        for (Integer integer : list) {
            if (integer % shardingVO.getTotal() == shardingVO.getIndex()) {
                System.out.println("第" + shardingVO.getIndex() + "分片执行，执行数据为：" + integer);
            }
        }
//        System.out.println("广播执行");
        return ReturnT.SUCCESS;
    }

    public static List<Integer> getList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        return list;
    }
}
