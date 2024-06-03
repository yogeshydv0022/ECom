package com.work.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.payload.request.RegisterRequest;
import com.work.payload.response.UserResponse;
import com.work.service.AuthService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.userSignup(request));
    }
}
