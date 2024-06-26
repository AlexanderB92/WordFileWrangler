package org.amb.util;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoremFileGenerator {

    //Default params
    private int NUMBER_OF_FILES = 1;
    private int NUMBER_OF_WORDS = 200;
    private String FILENAME_PREFIX = "default_filename";
    private final String PATH_TO_OUT_DIR = Paths.get("").toAbsolutePath() + "/generated_data";


    public LoremFileGenerator(int numberOfFiles, int numberOfWords, String filenamePrefix) {
        this.NUMBER_OF_FILES = numberOfFiles;
        this.NUMBER_OF_WORDS = numberOfWords;
        this.FILENAME_PREFIX = filenamePrefix;
    }

    public void generateTextFiles() throws IOException {
        createOutDir();

        int fileCount = 1;
        Lorem lorem = LoremIpsum.getInstance();

        for(int i = 0; i < NUMBER_OF_FILES; i++) {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_TO_OUT_DIR + '/' + FILENAME_PREFIX + fileCount + ".txt"))) {
                for(int j = 0; j < NUMBER_OF_WORDS / 10; j++) {
                    String words = lorem.getWords(10,10);
                    writer.write(words);
                    writer.newLine();
                }
            }
            fileCount++;
        }
    }

    private void createOutDir() throws IOException {
        Path p = Paths.get(PATH_TO_OUT_DIR);
        Files.createDirectories(p);
    }
}
