package com.example.jaluzi.controllers;

import com.example.jaluzi.models.User;
import com.example.jaluzi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User newUser) {
        if (userService.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userService.registerUser(newUser);

        return ResponseEntity.ok("Registration successful");
    }
}