package com.hkdevelopers.ModernBlogApplication.repository.blog;
import com.hkdevelopers.ModernBlogApplication.model.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {

}
