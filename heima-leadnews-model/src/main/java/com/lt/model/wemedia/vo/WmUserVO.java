package com.lt.model.wemedia.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 21:08
 */
@Data
public class WmUserVO {
    private Integer id;
    private String name;
    private String nickname;
    private String image;
    private String email;
    private Date loginTime;
    private Date createdTime;
}
