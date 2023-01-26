package com.lt.wemedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 22:53
 */
public class ReptilesArticleTest {

    @Test
    public void test() throws IOException {
        System.setProperty("webdriver.chrome.driver", "D:\\LeStoreDownload\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://3g.163.com/touch/ent/?ver=c&clickfrom=index2018_header_main");
        Document document = Jsoup.parse(driver.getPageSource());
//        Document document = Jsoup.connect("https://3g.163.com/touch/ent/?ver=c&clickfrom=index2018_header_main")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
//                .get();
        Elements liElements = document.getElementsByTag("li");
        for (Element liElement : liElements) {
            Elements aElements = liElement.getElementsByTag("a");
            for (Element aElement : aElements) {
                String href = aElement.attr("href");
                System.out.println("文章详情页面：" + href);
            }
            Elements titleElements = liElement.getElementsByTag("h4");
            for (Element titleElement : titleElements) {
                System.out.println("文章标题：" + titleElement.text());
            }
            Elements imgElements = liElement.getElementsByTag("img");
            for (Element imgElement : imgElements) {
                String src = imgElement.attr("src");
                String dataSrc = imgElement.attr("data-src");
                System.out.println("文章封面 src：" + src);
                System.out.println("文章封面 dataSrc：" + dataSrc);
            }
        }
    }
}
