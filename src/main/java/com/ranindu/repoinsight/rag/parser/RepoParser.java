package com.ranindu.repoinsight.rag.parser;

import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class RepoParser {

    public List<String> readJavaFiles(String repoPath) {
        List<String> contents = new ArrayList<>();

        File folder = new File(repoPath);
        File[] files = folder.listFiles();

        if (files == null) return contents;

        for (File file : files) {
            if (file.isDirectory()) {
                contents.addAll(readJavaFiles(file.getAbsolutePath()));
            } else if (file.getName().endsWith(".java")) {
                try {
                    String content = Files.readString(file.toPath());
                    contents.add(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return contents;
    }
}