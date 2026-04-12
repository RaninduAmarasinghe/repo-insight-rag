package com.ranindu.repoinsight.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {
    private WebClient webClient;

    public EmbeddingService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:11434").build();
    }

    public List<Double> getEmbedding(String text){
        Map<String, Object> request = new HashMap<>();
        request.put("model","mxbai-embed-large");
        request.put("prompt",text);

        return webClient.post()
                .uri("/api/embeddings")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> (List<Double>) res.get("embedding"))
                .block();
    }
}
   