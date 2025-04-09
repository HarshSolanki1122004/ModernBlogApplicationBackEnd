package com.hkdevelopers.ModernBlogApplication.repository.blog;
import com.hkdevelopers.ModernBlogApplication.model.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByUser_Id(Long userId);
}
