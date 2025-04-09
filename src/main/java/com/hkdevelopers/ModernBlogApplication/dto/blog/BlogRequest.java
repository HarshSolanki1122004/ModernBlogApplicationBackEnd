package com.hkdevelopers.ModernBlogApplication.dto.blog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Author is mandatory")
    private String author;
    @NotBlank(message = "Content is mandatory")
    private String content;
    @NotNull(message = "User ID is mandatory")
    private Long userId;
}
