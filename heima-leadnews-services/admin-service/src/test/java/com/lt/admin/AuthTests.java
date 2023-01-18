package com.lt.admin;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 19:09
 */
public class AuthTests {

    @Test
    public void idCardTest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apicode", "");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> map = new HashMap<>(2);
        map.put("idNumber", "");
        map.put("userName", "");
        // 封装请求参数
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(map), httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.yonyoucloud.com/apis/dst/matchIdentity/matchIdentity", entity, String.class);
        System.out.println(response);
    }

    @Test
    public void orcTest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apicode", "");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON); // json
        Map<String, String> map = new HashMap<>();
        map.put("image", "https://hmtt122.oss-cn-shanghai.aliyuncs.com/demo_idcard.png");
        map.put("imageType", "URL");
        map.put("ocrType", "0");
        HttpEntity<String> formEntry = new HttpEntity<>(JSON.toJSONString(map), httpHeaders); // 封装请求参数
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.yonyoucloud.com/apis/dst/IdcardOCR/IdcardOCR", formEntry, String.class);// 发送一个post请求
        System.out.println(response);
    }
}
