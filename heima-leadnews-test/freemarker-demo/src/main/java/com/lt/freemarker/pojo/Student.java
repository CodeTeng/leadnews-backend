package com.lt.freemarker.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/25 21:46
 */
@Data
public class Student {
    private String name;
    private int age;
    private Date birthday;
    private Float money;
}
