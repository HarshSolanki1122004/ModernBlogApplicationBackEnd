package com.hkdevelopers.ModernBlogApplication.service.blog;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogRequest;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogResponse;
import com.hkdevelopers.ModernBlogApplication.exceptions.ResourceNotFoundException;
import com.hkdevelopers.ModernBlogApplication.model.blog.Blog;
import com.hkdevelopers.ModernBlogApplication.model.user.User;
import com.hkdevelopers.ModernBlogApplication.repository.blog.BlogRepository;
import com.hkdevelopers.ModernBlogApplication.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public BlogResponse createBlog(BlogRequest blogRequest) {
        User user = userRepository.findById(blogRequest.getUserId()).orElseThrow(
                ()->new ResourceNotFoundException("User Not Found"));
        Blog blog = Blog.builder()
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .author(blogRequest.getAuthor())
                .user(user)
                .build();
        Blog savedBlog = blogRepository.save(blog);
        return mapToResponse(savedBlog);
    }
    @Override
    public Page<BlogResponse> getAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Blog> blogsPage = blogRepository.findAll(pageable);
        return blogsPage.map(this::mapToResponse);
    }

    @Override
    public BlogResponse getBlogsById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        return mapToResponse(blog);
    }

    @Override
    public BlogResponse updateBlog(Long id, BlogRequest blogRequest) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        if (blogRequest.getUserId() != null &&
                (blog.getUser() == null || !blog.getUser().getId().equals(blogRequest.getUserId()))) {
            User user = userRepository.findById(blogRequest.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + blogRequest.getUserId()));
            blog.setUser(user);
        }
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setAuthor(blogRequest.getAuthor());
        Blog updatedBlog = blogRepository.save(blog);
        return mapToResponse(updatedBlog);
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        blogRepository.delete(blog);
    }
    @Override
    public List<BlogResponse> getAllBlogsByUserId(Long userId) {
        // Verify user exists first
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        // Get all blogs for the user and convert to DTOs
        return blogRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    private BlogResponse mapToResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .author(blog.getAuthor())
                .userId(blog.getUser() != null ? blog.getUser().getId() : null)
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }
}
