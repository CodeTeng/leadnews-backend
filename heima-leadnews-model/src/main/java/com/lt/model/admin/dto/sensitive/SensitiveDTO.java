package com.lt.model.admin.dto.sensitive;

import com.lt.model.common.dto.PageRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/14 16:06
 */
@Data
public class SensitiveDTO extends PageRequestDTO {
    @ApiModelProperty("敏感词名称")
    private String name;
}
