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
 * @date: 2023/1/28 19:20
 */
@Data
@Document("ap_behavior_entry")
@ApiModel("行为实体")
public class ApBehaviorEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("行为实体主键id")
    private String id;
    @ApiModelProperty("实体类型 0-终端设备 1-用户")
    private Short type;
    @ApiModelProperty("关联id type=0 设备id  type=1 用户id")
    private Integer refId;
    @ApiModelProperty("创建时间")
    private Date createdTime;

    public enum Type {
        /**
         * 实体类型
         */
        USER((short) 1), EQUIPMENT((short) 0);
        @Getter
        Short code;

        Type(short code) {
            this.code = code;
        }
    }

    /**
     * 是否是用户
     *
     * @return true 是 false 否
     */
    public boolean isUser() {
        return this.getType() != null && this.getType() == Type.USER.getCode();
    }
}
