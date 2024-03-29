package com.melody.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginDTO implements Serializable {
        @ApiModelProperty("教师用户名")
        private String username;

        @ApiModelProperty("教师密码")
        private String password;
}
