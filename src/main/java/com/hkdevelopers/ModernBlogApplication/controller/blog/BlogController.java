package com.hkdevelopers.ModernBlogApplication.controller.blog;

import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogRequest;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogResponse;
import com.hkdevelopers.ModernBlogApplication.service.blog.BlogService;
import com.hkdevelopers.ModernBlogApplication.service.blog.ReportService;
import com.hkdevelopers.ModernBlogApplication.service.gemini.GeminiApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final GeminiApiService geminiApiService;
    private final ReportService reportService;


    @PostMapping("/blogs")
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody BlogRequest blogRequest) {
        BlogResponse createdBlog = blogService.createBlog(blogRequest);
        return ResponseEntity.ok(createdBlog);
    }

    @GetMapping("/blogs")
    public ResponseEntity<Page<BlogResponse>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BlogResponse> blogsPage = blogService.getAllBlogs(page, size);
        return ResponseEntity.ok(blogsPage);
    }

    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable Long blogId) {
        BlogResponse blogResponse = blogService.getBlogsById(blogId);
        return ResponseEntity.ok(blogResponse);
    }

    @PutMapping("/blogs/{blogId}")
    public ResponseEntity<BlogResponse> updateBlog(
            @PathVariable Long blogId,
            @Valid @RequestBody BlogRequest blogRequest) {
        BlogResponse updatedBlog = blogService.updateBlog(blogId, blogRequest);
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long blogId) {
        blogService.deleteBlog(blogId);
        return ResponseEntity.ok("Blog deleted successfully.");
    }

    @GetMapping("/blogs/{blogId}/summary")
    public ResponseEntity<String> getBlogSummary(@PathVariable Long blogId) {
        BlogResponse blog = blogService.getBlogsById(blogId);
        String summary = geminiApiService.summarizeBlog(blog.getContent());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/users/{userId}/blogs")
    public ResponseEntity<List<BlogResponse>> getUserBlogs(@PathVariable Long userId) {
        return ResponseEntity.ok(blogService.getAllBlogsByUserId(userId));
    }


    @GetMapping("/top-words/{userId}")
    public ResponseEntity<Map<String, Integer>> getTopWords(@PathVariable Long userId) {
        Map<String, Integer> topWords = reportService.getTopWordsForUser(userId);
        return ResponseEntity.ok(topWords);
    }

}