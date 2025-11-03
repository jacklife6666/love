package com.love.datingapp.common.dto;

import lombok.Data;
// 注意：这里从 javax 改为 jakarta
import jakarta.validation.constraints.NotEmpty;

@Data
public class LoginDTO {
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    @NotEmpty(message = "密码不能为空")
    private String password;
}