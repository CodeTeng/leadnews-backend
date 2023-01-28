package com.lt.model.behavior.pojo;

import com.lt.model.common.validator.ValidatorAddGroup;
import com.lt.model.common.validator.ValidatorUpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 19:24
 */
@Data
@Document("ap_likes_behavior")
@ApiModel("行为点赞实体")
public class ApLikesBehavior implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("关联的行为实体id")
    @NotBlank(message = "关联行为实体id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private String entryId;
    @ApiModelProperty("关联的文章id")
    @NotNull(message = "关联文章id不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private Long articleId;
    @ApiModelProperty("点赞内容类型 0-文章 1-动态")
    @NotNull(message = "点赞内容类型不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    @Range(min = 0, max = 2, message = "内容类型不符合")
    private Short type;
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
