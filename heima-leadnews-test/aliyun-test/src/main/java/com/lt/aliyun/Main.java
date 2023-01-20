package com.lt.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 21:38
 */
public class Main {
    public static void main(String[] args) throws Exception {
        IClientProfile profile = DefaultProfile
                .getProfile("cn-shanghai", "LTAI5tBBQNpZsVmk78pfc9Y4", "tc522gz9dtyG8rTOxSsuy01yRHPEUr");
        DefaultProfile
                .addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        // 指定API返回格式
        textScanRequest.setAcceptFormat(FormatType.JSON);
        textScanRequest.setHttpContentType(FormatType.JSON);
        // 指定请求方法
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST);
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String, Object> task1 = new LinkedHashMap<>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符。
         */
        task1.put("content", "我是一个文本,买卖冰毒是违法的");
        tasks.add(task1);
        JSONObject data = new JSONObject();
        /**
         * 检测场景。文本垃圾检测请传递antispam。
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        System.out.println("参数：" + JSON.toJSONString(data, true));
        textScanRequest.setHttpContent(data.toJSONString().getBytes(StandardCharsets.UTF_8), "UTF-8", FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), StandardCharsets.UTF_8));
                System.out.println("结果：" + JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject) sceneResult).getString("scene");
                                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                                //根据scene和suggetion做相关处理。
                                //suggestion == pass表示未命中垃圾。suggestion == block表示命中了垃圾，可以通过label字段查看命中的垃圾分类。
                                System.out.println("args = [" + scene + "]");
                                System.out.println("args = [" + suggestion + "]");
                            }
                        } else {
                            System.out.println("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
