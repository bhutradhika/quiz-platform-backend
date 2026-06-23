package com.quiz.platform.feature_core.controllers;

import com.quiz.platform.shared.dtos.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        ApiResponse response = new ApiResponse();
        response.setData("OK");
        return ResponseEntity.ok(response);
    }
}
