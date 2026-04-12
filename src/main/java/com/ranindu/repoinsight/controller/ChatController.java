package com.ranindu.repoinsight.controller;

import com.ranindu.repoinsight.service.EmbeddingService;
import com.ranindu.repoinsight.service.LlmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final LlmService llmService;
    private final EmbeddingService embeddingService;

    public ChatController(LlmService llmService, EmbeddingService embeddingService) {
        this.llmService = llmService;
        this.embeddingService = embeddingService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return llmService.askLlama(message);
    }

    @GetMapping("/embed")
    public List<Double> embed(@RequestParam String text) {
        return embeddingService.getEmbedding(text);
    }
}

