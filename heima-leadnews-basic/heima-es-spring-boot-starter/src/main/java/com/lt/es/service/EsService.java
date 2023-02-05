package com.lt.es.service;

import com.lt.model.common.vo.PageResponseResult;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 15:37
 */
public interface EsService<T> {
    void createIndex(String source, String indexName);

    boolean existIndex(String indexName);

    void deleteIndex(String indexName);

    void save(T t, String indexName);

    void saveBatch(List<T> list, String indexName);

    void deleteById(String id, String indexName);

    T getById(String id, Class<T> tClass, String indexName);

    PageResponseResult search(SearchSourceBuilder sourceBuilder, Class<T> tClass, String indexName);
}
