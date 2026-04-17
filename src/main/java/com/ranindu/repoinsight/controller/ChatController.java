package com.ranindu.repoinsight.controller;

import com.ranindu.repoinsight.rag.chunker.CodeChunker;
import com.ranindu.repoinsight.rag.parser.RepoParser;
import com.ranindu.repoinsight.rag.service.RagService;
import com.ranindu.repoinsight.rag.vectro.VectorStore;
import com.ranindu.repoinsight.service.EmbeddingService;
import com.ranindu.repoinsight.service.LlmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final LlmService llmService;
    private final EmbeddingService embeddingService;
    private final RagService ragService;

    public ChatController(LlmService llmService, EmbeddingService embeddingService, RagService ragService) {
        this.llmService = llmService;
        this.embeddingService = embeddingService;
        this.ragService = ragService;
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
    public int readRepo() {
        return ragService.countFiles("/Users/raninduamarasinghe/Desktop/npcverse-ai");
    }

    @GetMapping("/chunk")
    public int chunkRepo() {
        return ragService.countChunks("/Users/raninduamarasinghe/Desktop/npcverse-ai");
    }

    @PostMapping("/index")
    public String indexRepo() {
        return ragService.indexRepo("/Users/raninduamarasinghe/Desktop/npcverse-ai");
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return ragService.askQuestion(question);
    }
}

