package com.lt.model.behavior.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 19:31
 */
@Data
@Document("ap_collection")
@ApiModel("收藏行为实体")
public class ApCollection implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("关联的行为实体id")
    private String entryId;
    @ApiModelProperty("关联的文章id")
    private Long articleId;
    @ApiModelProperty("收藏内容类型 0-文章 1-动态")
    private Short type;
    @ApiModelProperty("创建时间")
    private Date collectionTime;

    public enum Type {
        ARTICLE((short) 0), DYNAMIC((short) 1);
        @Getter
        short code;

        Type(short code) {
            this.code = code;
        }
    }

    /**
     * 收藏的内容是否为文章
     */
    public boolean isCollectionArticle() {
        return this.getType() != null && this.getType() == Type.ARTICLE.getCode();
    }
}
