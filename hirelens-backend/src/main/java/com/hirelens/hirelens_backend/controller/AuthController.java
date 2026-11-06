
package com.hirelens.hirelens_backend.controller;

import com.hirelens.hirelens_backend.dto.UserRegistrationRequest;
import com.hirelens.hirelens_backend.dto.UserRegistrationResponse;
import com.hirelens.hirelens_backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        UserRegistrationResponse response =
                userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
