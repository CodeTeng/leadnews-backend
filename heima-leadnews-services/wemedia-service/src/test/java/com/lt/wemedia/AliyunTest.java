package com.lt.wemedia;

import com.lt.aliyun.GreenImageScan;
import com.lt.aliyun.GreenTextScan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/19 22:09
 */
@SpringBootTest
public class AliyunTest {
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;

    @Test
    public void testText() throws Exception {
        Map map = greenTextScan.greenTextScan("我是一个文本,冰毒买卖是违法的");
        System.out.println(map);
    }

    @Test
    public void testImage() throws Exception {
        List<String> images = new ArrayList<>();
        images.add("https://hmleadnews-project.oss-cn-shanghai.aliyuncs.com/material/2023/1/20230119/183a504134243891.jpg");
        images.add("https://hmleadnews-project.oss-cn-shanghai.aliyuncs.com/material/2023/1/20230119/6d17acf4e4c722dd.jpg");
        Map map = greenImageScan.imageUrlScan(images);
        System.out.println(map);
    }
}
