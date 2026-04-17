package com.ranindu.repoinsight.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChunkScore {
    private String chunk;
    private double score;
}
