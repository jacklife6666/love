package com.love.datingapp.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProfile {
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer gender; // 0-女, 1-男
    private LocalDate birthdate;
    private String city;
    private Integer height;
    private String education;
    private String occupation;
    private String introduction;
    private LocalDateTime updateTime;
}