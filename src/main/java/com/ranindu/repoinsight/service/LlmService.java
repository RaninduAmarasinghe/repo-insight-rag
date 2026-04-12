package com.ranindu.repoinsight.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class LlmService {

    private final WebClient webClient;
    private final WebClient.Builder builder;

    public LlmService(WebClient.Builder builder) {
        this.builder = builder;
        this.webClient = builder.baseUrl("http://localhost:11434").build();
            }

            public String askLlama(String prompt){
                Map<String, Object> request = new HashMap<>();
                request.put("model", "llama3.2");
                request.put("prompt", prompt);
                request.put("stream", false);

                return webClient.post()
                        .uri("/api/generate")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(res -> (String) res.get("response"))
                        .block();
            }
}
