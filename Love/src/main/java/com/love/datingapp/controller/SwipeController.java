package com.love.datingapp.controller;

import com.love.datingapp.common.dto.SwipeDTO;
import com.love.datingapp.service.impl.SwipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/swipes")
public class SwipeController {

    @Autowired
    private SwipeService swipeService;

    @PostMapping
    public ResponseEntity<?> handleSwipe(@AuthenticationPrincipal Long currentUserId,
                                         @Valid @RequestBody SwipeDTO swipeDTO) {
        boolean isMatched = swipeService.swipe(currentUserId, swipeDTO);
        // 返回一个JSON，告诉前端是否匹配成功
        return ResponseEntity.ok(Map.of("matched", isMatched));
    }
}