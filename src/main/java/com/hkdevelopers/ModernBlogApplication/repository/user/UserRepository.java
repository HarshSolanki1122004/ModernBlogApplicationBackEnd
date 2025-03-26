package com.hkdevelopers.ModernBlogApplication.repository.user;
import com.hkdevelopers.ModernBlogApplication.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
}
