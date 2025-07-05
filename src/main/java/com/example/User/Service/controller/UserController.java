package com.example.User.Service.controller;

import com.example.User.Service.entity.User;
import com.example.User.Service.service.JwtService;
import com.example.User.Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    private final JwtService jwtService;


    @GetMapping("/profile")
    public ResponseEntity<User> profile(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register-student")
    public ResponseEntity<Map<String, Object>> registerStudent(@RequestBody User user) {
        User createdStudent = userService.createStudent(user);

        String token = jwtService.generateToken(createdStudent.getUsername(), createdStudent.getRole().name());

        return ResponseEntity.ok(Map.of(
                "user", createdStudent,
                "token", token,
                "message", "Registration successful"
        ));
    }

    // ============== Admin Apis ==============
    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/createInstructor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createInstructor(@RequestBody User user) {
        User createInstructor = userService.createInstructor(user);
        return ResponseEntity.ok(createInstructor);
    }
}