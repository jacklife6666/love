package com.love.datingapp.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {
    private Long userId;
    private String phone;
    private String token;
    private String role; // 【新增】用户角色字段
}
// ...
