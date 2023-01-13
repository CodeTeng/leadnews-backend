package com.lt.utils.common;

import java.util.Base64;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/13 17:29
 */
public class Base64Utils {
    /**
     * 解码
     */
    public static byte[] decode(String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] b = decoder.decode(base64);
            // Base64解码
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 编码
     */
    public static String encode(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}
