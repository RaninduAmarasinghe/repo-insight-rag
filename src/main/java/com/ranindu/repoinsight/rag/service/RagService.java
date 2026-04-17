package com.ranindu.repoinsight.rag.service;

import com.ranindu.repoinsight.rag.chunker.CodeChunker;
import com.ranindu.repoinsight.rag.model.ChunkScore;
import com.ranindu.repoinsight.rag.parser.RepoParser;
import com.ranindu.repoinsight.rag.vectro.SimilarityUtil;
import com.ranindu.repoinsight.rag.vectro.VectorStore;
import com.ranindu.repoinsight.service.EmbeddingService;
import com.ranindu.repoinsight.service.LlmService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RagService {

    private final EmbeddingService embeddingService;
    private final LlmService llmService;
    private final VectorStore vectorStore;
    private final RepoParser repoParser;
    private final CodeChunker codeChunker;

    public RagService(EmbeddingService embeddingService, LlmService llmService, VectorStore vectorStore,  RepoParser repoParser, CodeChunker codeChunker) {
        this.embeddingService = embeddingService;
        this.llmService = llmService;
        this.vectorStore = vectorStore;
        this.repoParser = repoParser;
        this.codeChunker = codeChunker;
    }

    // ReadRepo
    public int countFiles(String path) {
        return repoParser.readCodeFiles(path).size();
    }


//chunkRepo
    public int countChunks(String path) {
        List<String> files = repoParser.readCodeFiles(path);

        int total = 0;
        for (String file : files) {
            total += codeChunker.chunkByLines(file).size();
        }
        return total;
    }

    //IndexRepo
    public String indexRepo(String path) {

        List<String> files = repoParser.readCodeFiles(path);

        int count = 0;
        int limit = 100;

        for (String file : files) {
            List<String> chunks = codeChunker.chunkByLines(file);

            for (String chunk : chunks) {

                if (count++ > limit) break;

                if (chunk.length() > 2000) continue;

                try {
                    List<Double> embedding = embeddingService.getEmbedding(chunk);
                    vectorStore.add(embedding, chunk);

                    Thread.sleep(100);

                } catch (Exception e) {
                    System.out.println("Skipped chunk due to error");
                }
            }
        }

        return "Indexed " + count + " chunks";
    }


    //for ask
    public String askQuestion(String question) {

        List<Double> questionEmbedding = embeddingService.getEmbedding(question);

        List<List<Double>> embeddings = vectorStore.getEmbeddings();
        List<String> chunks = vectorStore.getChunks();

        List<ChunkScore> scoredChunks = new ArrayList<>();

        for (int i = 0; i < embeddings.size(); i++) {
            double score = SimilarityUtil.cosineSimilarity(questionEmbedding, embeddings.get(i));
            scoredChunks.add(new ChunkScore(chunks.get(i), score));
        }


        scoredChunks.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        StringBuilder context = new StringBuilder();
        int topK = 3;

        for (int i = 0; i < Math.min(topK, scoredChunks.size()); i++) {
            context.append(scoredChunks.get(i).getChunk()).append("\n\n");
        }


        String prompt = buildPrompt(context.toString(), question);

        return llmService.askLlama(prompt);
    }

    private String buildPrompt(String context, String question) {
        return """
You are a senior software engineer.

Answer ONLY using the provided code.

If not found, say:
"I cannot find this in the provided code."

---------------------
CODE CONTEXT:
%s
---------------------

QUESTION:
%s
""".formatted(context, question);
    }
}
