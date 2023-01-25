package com.lt.wemedia;

import com.lt.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/25 22:40
 */
@SpringBootTest
public class MinIoTest {
    // 指定MinIo实现
    @Resource(name = "minIOFileStorageService")
    private FileStorageService fileStorageService;
    // 不指定 beanName 注入的是OSS的实现
    @Autowired
    private FileStorageService fileStorageService2;
    @Value("${file.minio.readPath}")
    private String readPath;

    @Test
    public void uploadToMinIo() throws FileNotFoundException {
        System.out.println(fileStorageService);
        System.out.println(fileStorageService2);
        // 准备好一个静态页
        FileInputStream fileInputStream = new FileInputStream("D://list.html");
        // 将静态页上传到minIO文件服务器中          文件名称            文件类型             文件流
        String path = fileStorageService.store("test", "list.html", "text/html", fileInputStream);
        System.out.println(readPath + path);
    }
}
