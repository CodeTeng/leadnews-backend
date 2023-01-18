package com.lt.model.wemedia.dto;

import com.lt.model.common.dto.PageRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 23:11
 */
@Data
public class WmMaterialDTO extends PageRequestDTO {
    @ApiModelProperty("1-收藏 0-未收藏")
    private Short isCollection;
}