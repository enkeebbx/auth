package com.ably.auth.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginRequest {

    @ApiModelProperty(value = "아이디/전화번호/이메일", required = true, example = "01090082929")
    @NotEmpty
    private String key;

    @ApiModelProperty(value = "비밀번호", required = true, example = "1234")
    @NotEmpty
    private String password;
}
