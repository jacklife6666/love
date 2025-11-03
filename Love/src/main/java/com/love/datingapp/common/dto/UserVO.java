package com.love.datingapp.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String phone;
    private String role;
    // 【新增】增加 password 字段
    private String password;
    private Integer status;
    private LocalDateTime createTime;
}