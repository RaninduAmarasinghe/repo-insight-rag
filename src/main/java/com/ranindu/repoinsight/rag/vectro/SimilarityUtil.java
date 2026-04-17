package com.ranindu.repoinsight.rag.vectro;

import java.util.List;

public class SimilarityUtil {

    public static double cosineSimilarity(List<Double> a, List<Double> b) {
        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += Math.pow(a.get(i), 2);
            normB += Math.pow(b.get(i), 2);

        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
