package com.lt.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @description: 文件上传接口
 * @author: ~Teng~
 * @date: 2023/1/18 19:45
 */
public interface FileStorageService {

    /**
     * 文件上传
     *
     * @param prefix      文件上传前缀
     * @param filename    文件名称
     * @param inputStream 文件流
     * @return pathUrl 全路径
     */
    String store(String prefix, String filename, InputStream inputStream);


    /**
     * 文件上传
     *
     * @param prefix      文件上传前缀
     * @param filename    文件名称
     * @param contentType 文件类型 "image/jpg" 或"text/html"
     * @param inputStream 文件流
     * @return pathUrl 全路径
     */
    String store(String prefix, String filename, String contentType, InputStream inputStream);

    /**
     * 文件删除
     *
     * @param pathUrl 全路径
     */
    void delete(String pathUrl);


    /**
     * 批量文件删除
     *
     * @param pathUrls 全路径集合
     */
    void deleteBatch(List<String> pathUrls);

    /**
     * 下载文件
     *
     * @param pathUrl 全路径
     */
    InputStream downloadFile(String pathUrl);

    /**
     * 获取文件文本内容
     *
     * @param pathUrl 全路径
     */
    String getFileContent(String pathUrl) throws IOException;
}