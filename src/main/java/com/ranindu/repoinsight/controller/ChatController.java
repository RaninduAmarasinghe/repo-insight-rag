package com.ranindu.repoinsight.controller;

import com.ranindu.repoinsight.rag.chunker.CodeChunker;
import com.ranindu.repoinsight.rag.parser.RepoParser;
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
    private final RepoParser repoParser;
    private final CodeChunker codeChunker;
    private final VectorStore vectorStore;

    public ChatController(LlmService llmService, EmbeddingService embeddingService, RepoParser repoParser, CodeChunker codeChunker, VectorStore vectorStore) {
        this.llmService = llmService;
        this.embeddingService = embeddingService;
        this.repoParser = repoParser;
        this.codeChunker = codeChunker;
        this.vectorStore = vectorStore;
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

    @PostMapping("/index")
    public String indexRepo(){
        List<String> files = repoParser.readCodeFiles("/Users/raninduamarasinghe/Desktop/TPC");

        int count = 0;
        int limit = 100;

        for (String file : files) {
            List<String> chunks = codeChunker.chunkByLines(file);

            for (String chunk : chunks){
                if(count ++ > limit) break;

                if (chunk.length() > 2000) continue;

                try {
                    List<Double> embedding = embeddingService.getEmbedding(chunk);
                    vectorStore.add(embedding,chunk);

                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("Skipped chunk due to error");
                }
            }
        }
        return "Indexed" + count + "chunks";
        }
}

