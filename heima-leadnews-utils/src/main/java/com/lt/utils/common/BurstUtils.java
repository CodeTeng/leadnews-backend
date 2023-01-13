package com.lt.utils.common;

/**
 * @description: 分片桶字段算法
 * @author: ~Teng~
 * @date: 2023/1/13 17:29
 */
public class BurstUtils {
    public final static String SPLIT_CHAR = "-";

    /**
     * 用-符号链接
     */
    public static String encrypt(Object... fileds) {
        StringBuffer sb = new StringBuffer();
        if (fileds != null && fileds.length > 0) {
            sb.append(fileds[0]);
            for (int i = 1; i < fileds.length; i++) {
                sb.append(SPLIT_CHAR).append(fileds[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 默认第一组
     */
    public static String groudOne(Object... fileds) {
        StringBuffer sb = new StringBuffer();
        if (fileds != null && fileds.length > 0) {
            sb.append("0");
            for (int i = 0; i < fileds.length; i++) {
                sb.append(SPLIT_CHAR).append(fileds[i]);
            }
        }
        return sb.toString();
    }
}
