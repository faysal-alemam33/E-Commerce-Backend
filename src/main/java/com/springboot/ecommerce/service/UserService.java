package com.springboot.ecommerce.service;


import com.springboot.ecommerce.config.JwtUtil;
import com.springboot.ecommerce.dto.AuthRequest;
import com.springboot.ecommerce.dto.RegisterRequest;
import com.springboot.ecommerce.entity.User;
import com.springboot.ecommerce.repository.RoleRepository;
import com.springboot.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequest request){
//        Role role = roleRepository.findByName(request.getRole())
//                            .orElseThrow(()-> new RuntimeException("Role not found.."));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
//        user.getRole().add(role);

        userRepository.save(user);
    }

    // login using email and pass
    public User authenticate(AuthRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentiald");
        }
        return user;
    }

    public void update(String email, String name){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(name);
        userRepository.save(user);
    }

    public String getUserFromToken(String token) {
        // Validate the token
        if (JwtUtil.validateToken(token)) {
            // Extract user info (email or username) from the token
            return JwtUtil.extractEmail(token);
        }
        throw new RuntimeException("Invalid token");
    }

}
