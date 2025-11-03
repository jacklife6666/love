package com.love.datingapp.common.dto;

import lombok.Data;
// 注意：这里从 javax 改为 jakarta
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class UserProfileDTO {
    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    @NotNull(message = "性别不能为空")
    private Integer gender;

    @NotNull(message = "生日不能为空")
    @Past(message = "生日必须是过去的时间")
    private LocalDate birthdate;

    private String city;
    private Integer height;
    private String education;
    private String occupation;
    private String introduction;
}