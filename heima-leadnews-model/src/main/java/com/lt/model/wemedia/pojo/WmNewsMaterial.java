package com.lt.model.wemedia.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 23:27
 */
@Data
@TableName("wm_news_material")
@ApiModel("自媒体图文引用素材实体")
public class WmNewsMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer id;

    @TableField("material_id")
    @ApiModelProperty("素材id")
    private Integer materialId;

    @TableField("news_id")
    @ApiModelProperty("图文id")
    private Integer newsId;

    @TableField("type")
    @ApiModelProperty("引用类型 0-内容引用 1-主图引用")
    private Short type;

    @TableField("ord")
    @ApiModelProperty("引用排序")
    private Short ord;
}
