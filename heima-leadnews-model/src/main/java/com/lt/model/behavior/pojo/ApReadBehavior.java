package com.lt.model.behavior.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 19:26
 */
@Data
@Document("ap_read_behavior")
@ApiModel("行为阅读实体")
public class ApReadBehavior implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("关联的行为实体id")
    private String entryId;
    @ApiModelProperty("关联的文章id")
    private Long articleId;
    @ApiModelProperty("阅读次数")
    private Short count;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("更新时间")
    private Date updatedTime;
}
