package com.ranindu.repoinsight.rag.chunker;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CodeChunker {

    public List<String> chunkByLines(String content){
        List<String> chunks = new ArrayList<>();

        String[] lines = content.split("\n");
        StringBuilder currentChunk = new StringBuilder();

        int maxLines = 30;
        int count = 0;
        for (String line : lines) {
            currentChunk.append(line).append("\n");
            count++;

            if (count == maxLines){
                chunks.add(currentChunk.toString());
                currentChunk.setLength(0);
                count = 0;
            }
        }
        if(!currentChunk.isEmpty()){
            chunks.add(currentChunk.toString());
        }
        return chunks;
    }
}
