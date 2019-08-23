package org.jeff.spring.security.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserQueryCondition {

    @ApiModelProperty(value = "用户姓名查询")
    private String username;

}
