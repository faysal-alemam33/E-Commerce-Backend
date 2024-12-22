package com.springboot.ecommerce.controller;


import com.springboot.ecommerce.config.JwtUtil;
import com.springboot.ecommerce.dto.AuthRequest;
import com.springboot.ecommerce.dto.RegisterRequest;
import com.springboot.ecommerce.entity.User;
import com.springboot.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        userService.registerUser(request);
        return ResponseEntity.ok("User registerd successfully ");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        User user = userService.authenticate(request);
        String token = jwtUtil.generateToken(user.getEmail());
        return  ResponseEntity.ok(Map.of("token", token));
    }

}
