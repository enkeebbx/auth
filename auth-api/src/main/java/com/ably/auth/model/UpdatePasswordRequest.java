package com.ably.auth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePasswordRequest {

    @ApiModelProperty(value = "전화번호", required = true, example = "01090082929")
    @NotEmpty
    private String phone;

    @ApiModelProperty(value = "새로 설정할 비밀번호", required = true, example = "123")
    @NotEmpty
    private String newPassword;
}
