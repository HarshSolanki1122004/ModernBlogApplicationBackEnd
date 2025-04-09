package com.hkdevelopers.ModernBlogApplication.service.blog;
import com.hkdevelopers.ModernBlogApplication.model.blog.Blog;
import com.hkdevelopers.ModernBlogApplication.repository.blog.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private BlogRepository blogRepository;

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "but", "is", "are", "was", "were",
            "to", "from", "in", "on", "at", "with", "as", "by", "for", "of",
            "it", "this", "that", "these", "those", "be", "been", "have", "has",
            "had", "i", "you", "he", "she", "we", "they", "them", "his", "her",
            "their", "our", "not", "so", "if", "then", "up", "down", "out", "about"
    );
    public Map<String, Integer> getTopWordsForUser(Long userId) {
        List<Blog> blogs = blogRepository.findByUser_Id(userId);

        Map<String, Integer> wordFrequency = new HashMap<>();

        for (Blog blog : blogs) {
            String content = blog.getContent().toLowerCase().replaceAll("[^a-zA-Z ]", " ");
            String[] words = content.split("\\s+");

            for (String word : words) {
                if (!STOP_WORDS.contains(word) && word.length() > 1) {
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                }
            }
        }

        // Sort and get top 5 words
        return wordFrequency.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
