package com.lt.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 22:01
 */
@Data
@Component
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties(prefix = "aliyun")
public class GreenTextScan {
    private String accessKeyId;
    private String secret;
    private static Map<String, String> labelsMap = new HashMap<>();

    static {
        labelsMap.put("normal", "正常文本");
        labelsMap.put("spam", "含垃圾信息");
        labelsMap.put("ad", "广告");
        labelsMap.put("politics", "社政");
        labelsMap.put("terrorism", "暴恐");
        labelsMap.put("abuse", "辱骂");
        labelsMap.put("porn", "色情");
        labelsMap.put("flood", "灌水");
        labelsMap.put("contraband", "违禁");
        labelsMap.put("meaningless", "无意义");
        labelsMap.put("harmful", "不良场景");
    }

    /**
     * 文本审核
     *
     * @param content 文本内容
     */
    public Map greenTextScan(String content) throws Exception {
        IClientProfile profile = DefaultProfile
                .getProfile("cn-shanghai", accessKeyId, secret);
        DefaultProfile
                .addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON);
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(MethodType.POST);
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String, Object> task1 = new LinkedHashMap<>();
        task1.put("dataId", UUID.randomUUID().toString());
        // 待检测的文本 内容不超过10000长度
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();
        // 检测场景，文本垃圾检测传递：antispam
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        textScanRequest.setHttpContent(data.toJSONString().getBytes(StandardCharsets.UTF_8), "UTF-8", FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);
        Map<String, String> resultMap = new HashMap<>();
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), StandardCharsets.UTF_8));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            String filteredContent = ((JSONObject) taskResult).getString("filteredContent");
                            resultMap.put("filteredContent", filteredContent);
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject) sceneResult).getString("scene");
                                String label = ((JSONObject) sceneResult).getString("label");
                                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                                if (!"pass".equals(suggestion)) {
                                    resultMap.put("suggestion", suggestion);
                                    resultMap.put("label", label);
                                    resultMap.put("scene", scene);
                                    String reason = labelsMap.get(label);
                                    resultMap.put("reason", reason);
                                    return resultMap;
                                }
                            }
                        } else {
                            return null;
                        }
                    }
                    resultMap.put("suggestion", "pass");
                    return resultMap;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
