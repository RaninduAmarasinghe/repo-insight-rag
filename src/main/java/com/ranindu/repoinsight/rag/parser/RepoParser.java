package com.ranindu.repoinsight.rag.parser;

import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RepoParser {

    public List<String> readCodeFiles(String repoPath) {
        List<String> contents = new ArrayList<>();

        File folder = new File(repoPath);
        File[] files = folder.listFiles();

        if (files == null) return contents;

        Set<String> allowedExtensions = Set.of(
                ".java", ".js", ".jsx", ".ts", ".tsx",
                ".py", ".go", ".cpp", ".c", ".cs",
                ".html", ".css", ".json", ".yml", ".yaml"
        );

        for (File file : files) {

            if (file.getAbsolutePath().contains("node_modules") ||
                    file.getAbsolutePath().contains(".git") ||
                    file.getAbsolutePath().contains("target") ||
                    file.getAbsolutePath().contains("build")) {
                continue;
            }

            if (file.isDirectory()) {
                contents.addAll(readCodeFiles(file.getAbsolutePath()));
            } else {
                String name = file.getName().toLowerCase();

                for (String ext : allowedExtensions) {
                    if (name.endsWith(ext)) {
                        try {
                            String content = Files.readString(file.toPath());
                            contents.add(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }

        return contents;
    }
}