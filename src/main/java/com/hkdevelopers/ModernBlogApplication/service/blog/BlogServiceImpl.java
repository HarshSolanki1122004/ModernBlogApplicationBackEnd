package com.hkdevelopers.ModernBlogApplication.service.blog;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogRequest;
import com.hkdevelopers.ModernBlogApplication.dto.blog.BlogResponse;
import com.hkdevelopers.ModernBlogApplication.exceptions.ResourceNotFoundException;
import com.hkdevelopers.ModernBlogApplication.model.blog.Blog;
import com.hkdevelopers.ModernBlogApplication.repository.blog.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public BlogResponse createBlog(BlogRequest blogRequest) {
        Blog blog = Blog.builder()
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .author(blogRequest.getAuthor())
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
    private BlogResponse mapToResponse(Blog savedBlog) {
        return BlogResponse.builder()
                .id(savedBlog.getId())
                .title(savedBlog.getTitle())
                .content(savedBlog.getContent())
                .author(savedBlog.getAuthor())
                .createdAt(savedBlog.getCreatedAt())
                .updatedAt(savedBlog.getUpdatedAt())
                .build();
    }
}
