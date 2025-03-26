package com.hkdevelopers.ModernBlogApplication.service.gemini;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiApiService {
    @Value("${gemini.api.key}")
    private String API_KEY;
    @Value("${gemini.api.url}")
    private String API_URL;

    private final WebClient webClient;

    public GeminiApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String summarizeBlog(String content){
        String prompt = "Summarize the following blog in a concise and informative way:\n\n" + content;
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of(
                                "role", "user",
                                "parts", new Object[] { Map.of("text", prompt) }
                        )
                }
        );
        String response = webClient.post()
                .uri(API_URL+API_KEY)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractContent(response);
    }

    private String extractContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        }catch (Exception e){
            return "Error Processing request:" + e.getMessage();
        }
    }
}
