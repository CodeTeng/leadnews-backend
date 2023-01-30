package com.lt.model.mess.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/30 14:49
 */
@Data
public class NewBehaviorDTO {
    @ApiModelProperty("修改文章的字段类型")
    private BehaviorType type;
    @ApiModelProperty("文章id")
    private Long articleId;
    @ApiModelProperty("次数 +1 -1")
    private Integer add;

    public enum BehaviorType {
        /**
         * 行为类型
         */
        COLLECTION, COMMENT, LIKES, VIEWS;
    }
}
