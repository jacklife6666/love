package com.love.datingapp.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SwipeDTO {

    @NotNull(message = "目标用户ID不能为空")
    private Long targetUserId;

    @NotEmpty(message = "滑动类型不能为空")
    @Pattern(regexp = "^(like|pass)$", message = "滑动类型必须是 'like' 或 'pass'")
    private String swipeType;
}