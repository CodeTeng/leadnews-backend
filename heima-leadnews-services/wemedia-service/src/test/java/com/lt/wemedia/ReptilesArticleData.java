package com.lt.wemedia;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lt.file.service.FileStorageService;
import com.lt.model.threadlocal.WmThreadLocalUtils;
import com.lt.model.wemedia.dto.NewsAuthDTO;
import com.lt.model.wemedia.dto.SubmitWmNewsDTO;
import com.lt.model.wemedia.pojo.WmMaterial;
import com.lt.model.wemedia.pojo.WmNews;
import com.lt.model.wemedia.pojo.WmUser;
import com.lt.wemedia.mapper.WmMaterialMapper;
import com.lt.wemedia.service.WmNewsService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @description: 抓取新闻数据  用于app端演示
 * @author: ~Teng~
 * @date: 2023/1/26 23:19
 */
@SpringBootTest
public class ReptilesArticleData {
    @Autowired
    private WmNewsService wmNewsService;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;
    @Autowired
    private FileStorageService fileStorageService;
    @Value("${file.oss.web-site}")
    private String webSite;

    /**
     * 直接通过调用修改状态方法，将所有爬虫数据 设置人工审核通过
     */
    @Test
    public void authPassWmNews() {
        List<WmNews> list = wmNewsService.list(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getStatus, 3));
        for (WmNews wmNews : list) {
            NewsAuthDTO dto = new NewsAuthDTO();
            dto.setId(wmNews.getId());
            wmNewsService.updateStatus((short) 4, dto);
        }
    }

    private Document document = null;

    @BeforeEach
    public void initDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\LeStoreDownload\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://3g.163.com/touch/ent/sub/music?ver=c&clickfrom=index2018_header_main");
        document = Jsoup.parse(driver.getPageSource());
    }

    /**
     * 获取数据  并封装成wmNews
     */
    @Test
    public void reptilesData() throws IOException {
        // 模拟当前自媒体登录用户
        WmUser wmUser = new WmUser();
        wmUser.setId(1102);
        WmThreadLocalUtils.setUser(wmUser);
        // 找到指定class的所有元素
        Elements liElements = document.getElementsByTag("li");
        for (Element liElement : liElements) {
            String href = null;
            String title = null;
            Elements imgList = null;
            try {
                Element aElement = liElement.getElementsByTag("a").get(0);
                href = aElement.attr("href");
                System.out.println("娱乐新闻的 url 路径：" + href);
                Element titleElement = liElement.getElementsByTag("h4").get(0);
                title = titleElement.text();
                System.out.println("娱乐新闻的标题：" + title);
                imgList = liElement.getElementsByTag("img");
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            // 封装WmNewsDto对象
            SubmitWmNewsDTO submitWmNewsDTO = new SubmitWmNewsDTO();
            // 解析单个文章详情
            List<Map> contentMap = parseContent(href);
            submitWmNewsDTO.setContent(JSON.toJSONString(contentMap));
            // 封面图片集合
            List<String> urlList = new ArrayList<>();
            for (Element imgEle : imgList) {
                String src = imgEle.attr("data-src");
                System.out.println("封面图片url==>" + src);
                String fileName = uploadPic(src);
                if (StringUtils.isNotBlank(fileName)) {
                    // 如果上传图片路径不为空  创建素材信息
                    WmMaterial wmMaterial = new WmMaterial();
                    wmMaterial.setUserId(WmThreadLocalUtils.getUser().getId());
                    wmMaterial.setUrl(fileName);
                    wmMaterial.setType((short) 0);
                    wmMaterial.setIsCollection((short) 0);
                    wmMaterial.setCreatedTime(new Date());
                    wmMaterialMapper.insert(wmMaterial);
                    urlList.add(fileName);
                }
            }
            submitWmNewsDTO.setTitle(title);
            submitWmNewsDTO.setType((short) urlList.size());
            if (urlList.size() > 0) {
                submitWmNewsDTO.setImages(urlList);
            }
            submitWmNewsDTO.setChannelId(51); // 设置频道
            try {
                Thread.sleep(1000); // 睡眠1秒 让发布时间不一致
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            submitWmNewsDTO.setPublishTime(new Date());
            submitWmNewsDTO.setStatus((short) 3); // 待审核状态
            submitWmNewsDTO.setLabels("爬虫");
            wmNewsService.submitNews(submitWmNewsDTO);
        }
    }

    /**
     * 封面图片url==>//nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0227%2Fd5ba8ca7j00q6cggh000vc000hs00b7m.jpg&thumbnail=234x146&quality=85&type=jpg
     */
    public String uploadPic(String imgUrl) {
        String imageUrl = getImageUrl(imgUrl);
        InputStream in = getInputStreamByUrl(imageUrl);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String suffix = imageUrl.substring(imageUrl.lastIndexOf("."));
        if (in != null) {
            String fileName = fileStorageService.store("material", uuid + suffix, in);
            System.out.println("上传文件名称: " + fileName);
            return fileName;
        }
        return null;
    }

    public String getImageUrl(String url) {
        try {
//            String url = "//nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0227%2Fd5ba8ca7j00q6cggh000vc000hs00b7m.jpg&thumbnail=234x146&quality=85&type=jpg";
            url = url.split("&")[0];
            url = url.substring(url.indexOf("url=")).replaceAll("url=", "");
            url = URLDecoder.decode(url, "utf8");
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析图片路径出现异常");
        }
    }

    /**
     * 工具方法:  根据url路径 获取输入流
     */
    public static InputStream getInputStreamByUrl(String strUrl) {
        if (!strUrl.startsWith("http")) {
            strUrl = "http:" + strUrl;
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解析文章详情内容
     */
    private List<Map> parseContent(String href) {
        href = href.substring(4);
        String url = "https://3g.163.com/dy" + href;
        List<Map> contentMap = new ArrayList<>();
        Document document = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            document = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .get();
            Element articleElement = document.getElementsByTag("article").get(0);
            Elements pElements = articleElement.getElementsByTag("p");
            for (Element pElement : pElements) {
                pElement.removeClass("keyword-search");
                String text = pElement.text();
                stringBuilder.append(text);
            }
            System.out.println("文本信息：" + stringBuilder);
            Map map = new HashMap();
            map.put("type", "text");
            map.put("value", stringBuilder.toString());
            contentMap.add(map);
        } catch (Exception e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("type", "text");
            map.put("value", "测试文章内容");
            contentMap.add(map);
        }
        return contentMap;
    }
}
