package com.hkdevelopers.ModernBlogApplication.service.blog;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogRequest;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogResponse;
import org.springframework.data.domain.Page;

public interface BlogService {
    BlogResponse createBlog(BlogRequest blogRequest);
    Page<BlogResponse> getAllBlogs(int page,int size);
    BlogResponse getBlogsById(Long id);
    BlogResponse updateBlog(Long id, BlogRequest blogRequest);
    void deleteBlog(Long id);
}
