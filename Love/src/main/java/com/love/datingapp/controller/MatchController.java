package com.love.datingapp.controller;

import com.love.datingapp.common.dto.MatchVO;
import com.love.datingapp.service.impl.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity<List<MatchVO>> getMyMatches(@AuthenticationPrincipal Long currentUserId) {
        List<MatchVO> matches = matchService.getMyMatches(currentUserId);
        return ResponseEntity.ok(matches);
    }
}