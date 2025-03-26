package com.hkdevelopers.ModernBlogApplication.service.user;
import com.hkdevelopers.ModernBlogApplication.dto.user.LoginRequest;
import com.hkdevelopers.ModernBlogApplication.exceptions.UserRegistrationAndLoginException;
import com.hkdevelopers.ModernBlogApplication.model.user.User;
import com.hkdevelopers.ModernBlogApplication.repository.user.UserRepository;
import com.hkdevelopers.ModernBlogApplication.service.jwt.JWTService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    @Value("${main.domain}")
    private String domainName;

    public User registerUser(User user){
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()){
            throw new UserRegistrationAndLoginException("Email is already registered try with another email or Login");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        User savedUser = userRepository.save(user);
        String verificationUrl = domainName + "/users/verify?token=" + token;
        String subject = "🔑 Verify Your Email to Activate Your Account";
        String htmlBody = "<html><body>"
                + "<h2 style='color:#4CAF50;'>Welcome to Our Service!</h2>"
                + "<p style='font-size:16px;'>Thank you for registering with us. Please <a href='" + verificationUrl + "' style='color:#2196F3;'>click here</a> to verify your email address and complete your registration.</p>"
                + "<p>If you did not sign up, please ignore this email.</p>"
                + "</body></html>";

        emailService.sendHtmlEmail(user.getEmail(), subject, htmlBody);
        return savedUser;
    }
    public ResponseEntity<String> verifyUser(String token){
        Optional<User> emailToken= userRepository.findByVerificationToken(token);
        if (emailToken.isEmpty()){
            throw new UserRegistrationAndLoginException("Invalid Token");
        }
        User user = emailToken.get();
        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return ResponseEntity.ok("Email verified successfully");
    }
    public String loginUser(@Valid LoginRequest loginUserRequest){
        User user = userRepository.findByEmail(loginUserRequest.getEmail()).orElseThrow(
                ()-> new UserRegistrationAndLoginException("User Not Found"));
        if (!user.isVerified()){
            throw new UserRegistrationAndLoginException("Email is not verified");
        }
        if (!encoder.matches(loginUserRequest.getPassword(),user.getPassword())){
            throw new UserRegistrationAndLoginException("Invalid credentials");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(),loginUserRequest.getPassword()));
        if(!authentication.isAuthenticated()){
            throw new UserRegistrationAndLoginException("Bad Credentials");
        }
        return jwtService.generateToken(loginUserRequest.getEmail());
    }
}
