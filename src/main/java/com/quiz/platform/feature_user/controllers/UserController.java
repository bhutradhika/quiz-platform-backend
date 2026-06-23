package com.quiz.platform.feature_user.controllers;

import com.quiz.platform.feature_user.services.UserService;
import com.quiz.platform.shared.dtos.ApiResponse;
import com.quiz.platform.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getCurrentUser() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(userService.getCurrentUser(userId));
        return ResponseEntity.ok(response);
    }
}
