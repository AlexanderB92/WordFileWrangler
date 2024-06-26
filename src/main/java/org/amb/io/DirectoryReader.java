package org.amb.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class DirectoryReader {

    private static final String EXCLUDE_FILENAME = "exclude.txt";

    /**
     * Lists files in directory, excludes folders
     * @param path Path to target directory
     * @return Returns lists of filenames
     */
    public ArrayList<String> getDirectoryFileNames(String path) {
        ArrayList<String> txtfiles = new ArrayList<>();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    txtfiles.add(file.getName());
                }
            }
        }
        return txtfiles;
    }

    /**
     * Reads and fetches exclusionary words
     * @param path Path to target directory
     * @return Returns set of words to be excluded, empty set if file does not exist
     */
    public HashSet<String> getExclusions(String path) {
        HashSet<String> exclusions = new HashSet<String>();

        //No exclusions
        if(!Files.exists(Paths.get(path + "\\" + EXCLUDE_FILENAME))) {
            return exclusions;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(path + "\\" + EXCLUDE_FILENAME ))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                String[] words = line.split("\\W+");
                Collections.addAll(exclusions, words);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exclusions;
    }
}
