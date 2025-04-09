package com.hkdevelopers.ModernBlogApplication.controller.user;
import com.hkdevelopers.ModernBlogApplication.dto.user.LoginRequest;
import com.hkdevelopers.ModernBlogApplication.dto.user.LoginResponse;
import com.hkdevelopers.ModernBlogApplication.model.user.User;
import com.hkdevelopers.ModernBlogApplication.service.user.EmailService;
import com.hkdevelopers.ModernBlogApplication.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("A verification link has been sent to your email address. Please check your inbox and verify your email to complete the registration process and enable login.");
    }
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token){
        return userService.verifyUser(token);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginUserRequest) {
        LoginResponse response = userService.loginUser(loginUserRequest);
        return ResponseEntity.ok(response);
    }
}
