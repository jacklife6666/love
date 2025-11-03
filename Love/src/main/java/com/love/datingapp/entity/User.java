package com.love.datingapp.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类, 对应数据库的 t_user 表
 * 这是项目的核心实体之一，用于存储用户的登录和基础认证信息。
 */
@Data // Lombok 注解: 自动生成 getter, setter, toString, equals, hashCode 等方法
public class User {

    /**
     * 用户ID, 主键
     * 对应数据库字段: `id` BIGINT
     */
    private Long id;

    /**
     * 手机号, 用于登录
     * 对应数据库字段: `phone` VARCHAR
     */
    private String phone;

    /**
     * 加密后的密码
     * 对应数据库字段: `password` VARCHAR
     */
    private String password;

    /**
     * 账户状态: 1-正常, 0-禁用
     * 对应数据库字段: `status` TINYINT
     */
    private Integer status;

    /**
     * 记录创建时间
     * 对应数据库字段: `create_time` DATETIME
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     * 对应数据库字段: `update_time` DATETIME
     */
    private LocalDateTime updateTime;
    // ...
    private String role; // 【新增】用户角色
}