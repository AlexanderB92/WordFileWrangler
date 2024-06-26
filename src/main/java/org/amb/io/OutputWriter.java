package org.amb.io;

import org.amb.model.WordCount;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class OutputWriter {

    public void writeFileFromList(String filename, String directoryPath, ArrayList<WordCount> wordList) throws IOException {
        Path dirPath = Paths.get(directoryPath);
        createOutDir(dirPath);
        Path filePath = dirPath.resolve(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for(WordCount w : wordList) {
                writer.write(w.getWord().toUpperCase() + " " + w.getCount());
                writer.newLine();
            }
        }
    }

    public void saveSimpleString (String filename, String directoryPath, String content) throws IOException {
        Path dirPath = Paths.get(directoryPath);
        createOutDir(dirPath);
        Path filePath = dirPath.resolve(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(content);
        }
    }

    private void createOutDir(Path dirPath) throws IOException {
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }
}
