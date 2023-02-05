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
 * @date: 2023/2/5 14:25
 */
@Data
@Document("ap_associate_words")
@ApiModel("联想词实体")
public class ApAssociateWords implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("联想词")
    private String associateWords;
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
