package com.example.demo.controller;

import com.example.demo.dto.UserProfileDto;
import com.example.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileDto getCurrentUser(Authentication authentication) {
        return userService.getCurrentUserProfile(authentication.getName());
    }
}