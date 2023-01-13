package com.lt.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/13 17:35
 */
public class FileUtils {
    /**
     * 重资源流中读取第一行内容
     */
    public static String readFirstLineFormResource(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        return br.readLine();
    }
}
