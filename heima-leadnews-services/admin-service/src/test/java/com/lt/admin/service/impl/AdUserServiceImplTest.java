package com.lt.admin.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 21:06
 */
public class AdUserServiceImplTest {
    @Test
    void login() {
        String salt = "123456";
        String pwd = "guest" + salt;
        String res = DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8));
        // 34e20b52f5bd120db806e57e27f47ed0
        System.out.println(res);
    }
}