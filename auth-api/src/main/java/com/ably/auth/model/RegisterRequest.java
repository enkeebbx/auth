package com.ably.auth.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class RegisterRequest {

    @ApiModelProperty(value = "이메일", required = true, example = "abc@gmail.com")
    @NotEmpty
    private String email;

    @ApiModelProperty(value = "아이디", required = true, example = "abc")
    @NotEmpty
    private String username;

    @ApiModelProperty(value = "비밀번호", required = true, example = "1234")
    @NotEmpty
    private String password;

    @ApiModelProperty(value = "이름", required = true, example = "홍길동")
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "전화번호", required = true, example = "01090082929")
    @NotEmpty
    private String phone;
}
