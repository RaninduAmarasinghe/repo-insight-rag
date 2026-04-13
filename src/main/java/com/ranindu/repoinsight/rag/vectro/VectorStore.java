package com.ranindu.repoinsight.rag.vectro;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VectorStore {

    private final List<List<Double>> embeddings = new ArrayList<>();
    private final List<String> chunks = new ArrayList<>();

    public void add(List<Double> embedding, String chunk) {
        embeddings.add(embedding);
        chunks.add(chunk);
    }
    public List<String> getChunks() {
        return chunks;
    }
    public List<List<Double>> getEmbeddings() {
        return embeddings;
    }
}
