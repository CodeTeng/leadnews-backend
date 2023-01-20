package com.lt.wemedia;

import com.lt.wemedia.service.WmNewsAutoScanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/20 14:58
 */
@SpringBootTest
public class WmNewsAutoScanServiceTest {
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Test
    public void autoScanWmNews() {
        wmNewsAutoScanService.autoScanWmNews(6271);
    }
}
