package com.ranindu.repoinsight.controller;

import com.ranindu.repoinsight.rag.parser.RepoParser;
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
    private final RepoParser repoParser;

    public ChatController(LlmService llmService, EmbeddingService embeddingService, RepoParser repoParser) {
        this.llmService = llmService;
        this.embeddingService = embeddingService;
        this.repoParser = repoParser;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return llmService.askLlama(message);
    }

    @GetMapping("/embed")
    public List<Double> embed(@RequestParam String text) {
        return embeddingService.getEmbedding(text);
    }

    @GetMapping("/read")
    public int readrepo(){
        List<String> files = repoParser.readJavaFiles("/Users/raninduamarasinghe/Desktop/TPC");
                return files.size();
    }
}

