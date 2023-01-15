package com.lt.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 18:10
 */
public class Md5Test {

    @Test
    public void md5() {
        String salt = RandomStringUtils.randomAlphanumeric(10);
        System.out.println("salt:" + salt);
        String str = DigestUtils.md5DigestAsHex(("hello" + salt).getBytes(StandardCharsets.UTF_8));
        System.out.println(str);
    }
}
