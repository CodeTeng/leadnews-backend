package com.lt.wemedia;

import com.lt.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 19:57
 */
@SpringBootTest
public class OssTests {
    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.oss.web-site}")
    String webSite;

    @Test
    public void testFileUpload() throws Exception {
        FileInputStream inputStream = new FileInputStream(new File("D:\\KaWaYiImage\\bg.jpg"));
        String store = fileStorageService.store("upload", "love.jpg", inputStream);
        System.out.println(webSite + store);

        // 删除文件
        fileStorageService.delete(store);
    }
}
