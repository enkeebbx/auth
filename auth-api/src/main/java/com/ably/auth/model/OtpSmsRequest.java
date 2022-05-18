package com.ably.auth.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class OtpSmsRequest {

    @ApiModelProperty(value = "전화번호", required = true, example = "01090082929")
    @NotEmpty
    private String phone;
}
