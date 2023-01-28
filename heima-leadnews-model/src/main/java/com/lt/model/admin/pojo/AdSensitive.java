package com.lt.model.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lt.model.common.validator.ValidatorAddGroup;
import com.lt.model.common.validator.ValidatorDeleteGroup;
import com.lt.model.common.validator.ValidatorUpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 15:58
 */
@Data
@TableName("ad_sensitive")
@ApiModel("敏感词实体")
public class AdSensitive implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    @NotNull(message = "主键id不能为空", groups = {ValidatorUpdateGroup.class})
    private Integer id;

    @TableField("sensitives")
    @ApiModelProperty(value = "敏感词", required = true)
    @NotBlank(message = "敏感词不能为空", groups = {ValidatorAddGroup.class, ValidatorUpdateGroup.class})
    private String sensitives;

    @TableField("created_time")
    @ApiModelProperty("创建时间")
    private Date createdTime;
}
