package com.ranindu.repoinsight.controller;

import com.ranindu.repoinsight.rag.chunker.CodeChunker;
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
    private final CodeChunker codeChunker;

    public ChatController(LlmService llmService, EmbeddingService embeddingService, RepoParser repoParser, CodeChunker codeChunker) {
        this.llmService = llmService;
        this.embeddingService = embeddingService;
        this.repoParser = repoParser;
        this.codeChunker = codeChunker;
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
        List<String> files = repoParser.readCodeFiles("/Users/raninduamarasinghe/Desktop/TPC");
                return files.size();
    }

    @GetMapping("/chunk")
    public int chunkRepo(){
        List<String> files = repoParser.readCodeFiles("/Users/raninduamarasinghe/Desktop/TPC");

        int totalChunk = 0;
        for (String file : files) {
            List<String> chunks = codeChunker.chunkByLines(file);
            totalChunk += chunks.size();
        }
        return totalChunk;
    }

}

