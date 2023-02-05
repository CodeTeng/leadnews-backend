package com.lt.model.search.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/5 13:26
 */
@Data
@Document("ap_user_search")
@ApiModel("APP用户搜索信息实体")
public class ApUserSearch implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("行为实体id")
    private String entryId;
    @ApiModelProperty("搜索词")
    private String keyword;
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
