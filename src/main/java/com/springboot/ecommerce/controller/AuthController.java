package com.springboot.ecommerce.controller;


import com.springboot.ecommerce.config.JwtUtil;
import com.springboot.ecommerce.dto.AuthRequest;
import com.springboot.ecommerce.dto.RegisterRequest;
import com.springboot.ecommerce.entity.User;
import com.springboot.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
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

    @PutMapping("/update")
    public ResponseEntity<String> updateUserName(@RequestHeader("Authorization") String authorizationHeader
                                                 ,@RequestParam String name){

        // Extract the token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");
        // Verify and extract the user from the token
        String email = userService.getUserFromToken(token);
        // Update the username
        userService.update(email, name);
        return ResponseEntity.status(HttpStatus.OK).body("name updated");
    }

}
