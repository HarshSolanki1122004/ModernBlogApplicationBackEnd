package com.hkdevelopers.ModernBlogApplication.dto.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Email(message = "invalid Email Address")
    @NotBlank(message = "Email Is Required")
    private String email;

    @NotBlank(message = "Password Is Required")
    private String password;
}
