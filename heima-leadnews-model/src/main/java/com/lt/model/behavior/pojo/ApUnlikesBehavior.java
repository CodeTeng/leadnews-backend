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
 * @date: 2023/1/28 19:29
 */
@Data
@Document("ap_unlikes_behavior")
@ApiModel("不喜欢行为实体")
public class ApUnlikesBehavior implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("关联的行为实体id")
    private String entryId;
    @ApiModelProperty("关联的文章id")
    private Long articleId;
    @ApiModelProperty("类型 0-不喜欢 1-取消不喜欢")
    private Short type;
    @ApiModelProperty("登录时间")
    private Date createdTime;

    /**
     * 定义不喜欢操作的类型
     */
    public enum Type {
        UNLIKE((short) 0), CANCEL((short) 1);
        @Getter
        short code;

        Type(short code) {
            this.code = code;
        }
    }
}
