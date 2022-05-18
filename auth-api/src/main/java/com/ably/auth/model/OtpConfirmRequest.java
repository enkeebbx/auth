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
public class OtpConfirmRequest {

    @ApiModelProperty(value = "전화번호", required = true, example = "01090082929")
    @NotEmpty
    private String phone;

    @ApiModelProperty(value = "OTP 번호", required = true, example = "\"623222\"")
    @NotEmpty
    private String otpNumber;
}
