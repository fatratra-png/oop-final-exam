package org.spring.oopfinalexam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OopFinalExamApplication {

    public static void main(String[] args) {
        loadDotEnv();
        SpringApplication.run(OopFinalExamApplication.class, args);
    }

    private static void loadDotEnv() {
        Path envFile = Path.of(".env");
        if (!Files.exists(envFile)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(envFile);
            for (String line : lines) {
                String trimmed = line.strip();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int separator = trimmed.indexOf('=');
                if (separator <= 0) {
                    continue;
                }
                String key = trimmed.substring(0, separator).strip();
                String value = trimmed.substring(separator + 1).strip();
                System.setProperty(key, value);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load .env file", e);
        }
    }
}
