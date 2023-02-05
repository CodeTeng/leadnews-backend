package com.lt.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.lt.es.service.EsService;
import com.lt.model.common.anno.EsId;
import com.lt.model.common.vo.PageResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 15:38
 */
@Service
@Slf4j
public class EsServiceImpl<T> implements EsService<T> {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建索引库
     */
    @Override
    public void createIndex(String source, String indexName) {
        try {
            // 1.创建Request对象
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // 2.准备参数，settings和mappings
            request.source(source, XContentType.JSON);
            // 3.发出请求
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            // 4.判断
            if (!response.isAcknowledged()) {
                // 创建索引库失败！
                throw new RuntimeException("索引库创建失败！");
            }
        } catch (IOException e) {
            throw new RuntimeException("索引库创建失败！", e);
        }
    }

    /**
     * 索引库是否存在
     */
    @Override
    public boolean existIndex(String indexName) {
        try {
            // 1.创建Request对象
            GetIndexRequest request = new GetIndexRequest(indexName);
            // 3.发出请求
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("索引库创建失败！", e);
        }
    }

    /**
     * 删除索引库
     */
    @Override
    public void deleteIndex(String indexName) {
        try {
            // 1.创建Request对象
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            // 2.发出请求
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("索引库删除失败！", e);
        }
    }

    /**
     * 文档的新增
     */
    @Override
    public void save(T t, String indexName) {
        try {
            // 1.创建Request对象
            IndexRequest request = new IndexRequest(indexName);
            // 2.准备参数，id和文档数据
            request.id(getId(t));
            request.source(JSON.toJSONString(t), XContentType.JSON);
            // 3.发出请求
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("新增文档失败！", e);
        }
    }

    /**
     * 文档批量新增
     *
     * @param list 数据集合
     */
    @Override
    public void saveBatch(List<T> list, String indexName) {
        try {
            // 1.创建Request对象
            BulkRequest request = new BulkRequest(indexName);
            // 2.准备参数
            for (T t : list) {
                request.add(new IndexRequest().id(getId(t)).source(JSON.toJSONString(t), XContentType.JSON));
            }
            // 3.发出请求
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("批量新增失败！", e);
        }
    }

    /**
     * 删除文档
     */
    @Override
    public void deleteById(String id, String indexName) {
        try {
            // 1.请求
            DeleteRequest request = new DeleteRequest(indexName, id);
            // 2.发出请求
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("删除文档失败！", e);
        }
    }

    /**
     * 获取文档
     */
    @Override
    public T getById(String id, Class<T> tClass, String indexName) {
        try {
            // 1.请求
            GetRequest request = new GetRequest(indexName, id);
            // 2.发出请求
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            // 3.解析结果
            String json = response.getSourceAsString();
            // 4.反序列化
            return JSON.parseObject(json, tClass);
        } catch (IOException e) {
            throw new RuntimeException("删除文档失败！", e);
        }
    }

    /**
     * 查询并返回结果，并且带高亮、分页、排序
     *
     * @param sourceBuilder 条件
     * @return 分页结果
     */
    @Override
    public PageResponseResult search(SearchSourceBuilder sourceBuilder, Class<T> clazz, String indexName) {
        try {
            // 封装分页结果
            PageResponseResult result = new PageResponseResult();
            int from = sourceBuilder.from();
            int size = sourceBuilder.size();
            result.setCurrentPage(from / size + 1);
            result.setSize(size);
            // 1.准备request
            SearchRequest request = new SearchRequest(indexName);
            // 2.准备参数
            request.source(sourceBuilder);
            // 3.发出请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 4.解析结果
            SearchHits searchHits = response.getHits();
            // 4.1.获取总条数
            long total = searchHits.getTotalHits().value;
            // 4.2.获取数据
            SearchHit[] hits = searchHits.getHits();
            List<T> list = new ArrayList<>(hits.length);
            // 4.3.循环处理
            for (SearchHit hit : hits) {
                // 获取source
                String json = hit.getSourceAsString();
                // 反序列化
                T t = JSON.parseObject(json, clazz);
                list.add(t);
                // 高亮处理
                handleHighlight(t, hit);
            }
            result.setTotal(total);
            result.setData(list);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("查询文档失败！", e);
        }
    }

    /**
     * 高亮处理
     */
    private void handleHighlight(T t, SearchHit hit) throws InvocationTargetException, IllegalAccessException {
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        for (HighlightField highlightField : highlightFields.values()) {
            // 获取高亮字段名称
            String fieldName = highlightField.getName();
            // 获取高亮结果
            String highlightValue = StringUtils.join(highlightField.getFragments());
            // 获取字节码
            BeanUtils.setProperty(t, fieldName, highlightValue);
        }
    }

    /**
     * 根据 EsId注解 获取 es 格式数据
     */
    private String getId(Object t) {
        try {
            Class<?> tClass = t.getClass();
            Field[] declaredFields = tClass.getDeclaredFields();
            Object id = null;
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.isAnnotationPresent(EsId.class)) {
                    id = declaredField.get(t);
                }
            }
            if (id == null) {
                log.error("es工具类出错， 未发现标记@EsId注解 ");
                throw new RuntimeException("实体类中必须包含@TableId注解！");
            }
            return id.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
