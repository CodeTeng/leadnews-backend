package com.lt.mongo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.lt.mongo.pojo.ApComment;
import com.mongodb.client.result.DeleteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 17:08
 */
@SpringBootTest
public class ApCommentTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsert() {
        List<ApComment> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ApComment apComment = new ApComment();
            apComment.setContent("这是测试添加的内容" + i);
            apComment.setLikes(new Random().nextInt(100));
            apComment.setReply(new Random().nextInt(100));
            apComment.setAddress("河南");
            apComment.setCreatedTime(new Date());
            list.add(apComment);
        }
        // insert 和 save 都可以添加 不过 save 比 insert 多了修改的功能，即 save 也可以修改
        mongoTemplate.insertAll(list);
    }

    @Test
    public void testUpdate() {
        ApComment apComment = new ApComment();
        apComment.setId("63d4e6efb3f7bf38ac5fdadb");
        apComment.setContent("这是测试修改的内容");
        apComment.setLikes(new Random().nextInt(100));
        apComment.setReply(new Random().nextInt(100));
        apComment.setAddress("河南");
        apComment.setCreatedTime(new Date());
        // save 也可以修改
//        mongoTemplate.save(apComment);
        // 也可以根据条件进行修改 updateFirst 如果有多个 只修改第一个
        // 查询条件
        Query query = Query.query(Criteria.where("id").is("63d4e6efb3f7bf38ac5fdadb").and("address").is("河南"));
        // 修改数据
        Update update = new Update();
        update.set("address", "深圳").set("createdTime", new Date());
        mongoTemplate.updateFirst(query, update, ApComment.class);
        // 如果查询的数据有多个要修改多个 可使用
//        mongoTemplate.updateMulti(query, update, ApComment.class);
    }

    @Test
    public void testDelete() {
        Query query = Query.query(Criteria.where("id").is("63d4e6efb3f7bf38ac5fdadb"));
        // 删除
        DeleteResult deleteResult = mongoTemplate.remove(query, ApComment.class);
        System.out.println(deleteResult);
    }

    @Test
    public void testFindOne() {
        Query query = Query.query(Criteria.where("address").is("河南"));
        // 查询一个 如果查询出来有多个的话 不会报错 给出第一个
        ApComment apComment = mongoTemplate.findOne(query, ApComment.class);
        System.out.println(apComment);
    }

    @Test
    public void testFindAll() {
        // 查询所有
        List<ApComment> list = mongoTemplate.findAll(ApComment.class);
        list.forEach(System.out::println);
    }

    @Test
    public void testFindByQuery() {
        // 注意这里的 is 查询 必须与 实体类型对应 即 Integer:95   String:"95"
        Query query = Query.query(Criteria.where("address").is("河南").and("likes").is(95));
        // 按条件查询
        List<ApComment> list = mongoTemplate.find(query, ApComment.class);
        list.forEach(System.out::println);
    }

    @Test
    public void testFindPage() {
        // 分页查询 有两种方式   一种有页码     一种没有页码
        Query query = Query.query(Criteria.where("address").is("河南"));
        // 方式一：有页码
        int page = 2;
        int size = 5;
        PageRequest pageRequest = PageRequest.of(page - 1, size);// page - 1 显示第一页 从 0 开始
        query.with(pageRequest);
        // 方式二：没有页码
//        query.skip(2); // 跳过前2个
//        query.limit(5); // 每页显示5跳数据
        // 排序 默认升序 可以调整为降序
        Sort sort = Sort.by(Sort.Direction.DESC, "likes");
        query.with(sort);
        List<ApComment> list = mongoTemplate.find(query, ApComment.class);
        list.forEach(System.out::println);
    }
}
