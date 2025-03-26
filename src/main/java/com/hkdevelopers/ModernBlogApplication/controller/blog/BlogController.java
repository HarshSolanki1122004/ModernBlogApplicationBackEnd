package com.hkdevelopers.ModernBlogApplication.controller.blog;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogRequest;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogResponse;
import com.hkdevelopers.ModernBlogApplication.service.blog.BlogService;
import com.hkdevelopers.ModernBlogApplication.service.gemini.GeminiApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final GeminiApiService geminiApiService;

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody BlogRequest blogRequest){
        BlogResponse createdBlog = blogService.createBlog(blogRequest);
        return ResponseEntity.ok(createdBlog);
    }

    @GetMapping
    public ResponseEntity<Page<BlogResponse>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BlogResponse> blogsPage = blogService.getAllBlogs(page, size);
        return ResponseEntity.ok(blogsPage);
    }

    @GetMapping("{id}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable Long id){
        BlogResponse blogResponse = blogService.getBlogsById(id);
        return ResponseEntity.ok(blogResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogResponse> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody BlogRequest blogRequest) {

        BlogResponse updatedBlog = blogService.updateBlog(id, blogRequest);
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok("Blog deleted successfully.");
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<String> getBlogSummary(@PathVariable Long id) {
        BlogResponse blog = blogService.getBlogsById(id);
        String summary = geminiApiService.summarizeBlog(blog.getContent());
        return ResponseEntity.ok(summary);
    }

}
