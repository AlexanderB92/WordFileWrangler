package org.amb.tools;

import org.amb.io.OutputWriter;
import org.amb.model.CounterResult;
import org.amb.io.DirectoryReader;
import org.amb.model.WordCount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Counters {

    private static final String DICT_FILE_SUFFIX = "FILE_";
    private static final String DICT_FILE_EXTENSION = ".txt";

    /**
     * Counts the number of occurrences of unique words in target file
     * @param pathToFile Path to the target file
     * @return HashMap where key are the found unique words, and the value is the aggregated count
     */
    public CounterResult countOccurrencesInFile(String pathToFile) {
        HashMap<String, Integer> occurrences = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                String[] words = line.split("\\W+");
                for (String word : words) {
                    if (occurrences.get(word) != null) {
                        occurrences.put(word, occurrences.get(word) + 1);
                    } else {
                        occurrences.put(word, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new CounterResult(occurrences, null, 0);
    }


    /**
     * Counts the number of occurrences of unique words in files with set extension (defined in util) in the target directory and exclusions
     * @param pathToDir Path to the target directory
     * @return CounterResult
     */
    public CounterResult countOccurrencesInDirectory(String pathToDir) {
        HashMap<String, Integer> occurrences = new HashMap<>();
        int excluded = 0;

        DirectoryReader r = new DirectoryReader();
        ArrayList<String> filenames = r.getDirectoryFileNames(pathToDir);
        HashSet<String> exclusions = r.getExclusions(pathToDir);

        for (String filename : filenames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathToDir + "\\" + filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.toLowerCase();
                    String[] words = line.split("\\W+");
                    for (String word : words) {
                        if(!word.isEmpty()) {
                            if (occurrences.get(word) != null) {
                                occurrences.put(word, occurrences.get(word) + 1);
                            } else if (exclusions.contains(word)) {
                                excluded++;
                            } else {
                                occurrences.put(word, 1);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new CounterResult(occurrences, null, excluded);
    }

    /**
     * Counts the number of occurrences of unique words in files with set extension (defined in util) in the target directory and exclusions
     * @param pathToDir Path to the target directory
     * @return CounterResult
     */
    public CounterResult countOccurrencesInDirectoryOrdered(String pathToDir) {
        TreeMap<String, Integer> occurrences = new TreeMap<>();
        int excluded = 0;

        DirectoryReader r = new DirectoryReader();
        ArrayList<String> filenames = r.getDirectoryFileNames(pathToDir);
        HashSet<String> exclusions = r.getExclusions(pathToDir);

        for (String filename : filenames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathToDir + "\\" + filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.toLowerCase();
                    String[] words = line.split("\\W+");
                    for (String word : words) {
                        if(!word.isEmpty()) {
                            if (occurrences.get(word) != null) {
                                occurrences.put(word, occurrences.get(word) + 1);
                            } else if (exclusions.contains(word)) {
                                excluded++;
                            } else {
                                occurrences.put(word, 1);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new CounterResult(null, occurrences , excluded);
    }

    /**
     * Counts only excluded words in the target directory
     * @param pathToDir Path to the target directory
     * @return int count of excluded words
     */
    public int countExcludedWords(String pathToDir) {
        int excluded = 0;
        DirectoryReader r = new DirectoryReader();
        ArrayList<String> filenames = r.getDirectoryFileNames(pathToDir);
        HashSet<String> exclusions = r.getExclusions(pathToDir);

        for (String filename : filenames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathToDir + "\\" + filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.toLowerCase();
                    String[] words = line.split("\\W+");
                    for (String word : words) {
                        if(exclusions.contains(word)) {
                            excluded++;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return excluded;
    }

    public int createFileDictionaryLike(CounterResult counterResult, String outDir) throws IOException {

        //Files written
        int written = 0;

        //Order unordered words in map
        Map<Character, List<WordCount>> orderedWords = new HashMap<>();

        for(Map.Entry<String, Integer> e : counterResult.getOccurences().entrySet()) {
            char firstLetter = e.getKey().charAt(0);

            //use computeIfAbsent, because ArrayList is a common expensive value
            orderedWords.computeIfAbsent(firstLetter, k -> new ArrayList<>()).add(new WordCount(e.getKey(), e.getValue()));
        }

        //Write to files
        OutputWriter outputWriter = new OutputWriter();
        for(Map.Entry<Character, List<WordCount>> orderedEntry : orderedWords.entrySet()) {
            String filename = DICT_FILE_SUFFIX + orderedEntry.getKey() + DICT_FILE_EXTENSION;
            outputWriter.writeFileFromList(filename, outDir + File.separator + "result_dict", (ArrayList<WordCount>) orderedEntry.getValue());
            written++;
        }
        return written;
    }

    public void createFileDictionaryLikeFromOrderedList(CounterResult counterResult, String outDir) throws IOException {
        int index = 0;
        char lastLetter = ' ';
        ArrayList<WordCount> currentGroup = new ArrayList<>();

        OutputWriter outputWriter = new OutputWriter();

        //One entry map (empty text files, or only 1)
        if(counterResult.getOrderedOccurrences().size() == 1) {
            char currentLetter = counterResult.getOrderedOccurrences().firstEntry().getKey().charAt(0);
            currentGroup.add(new WordCount(counterResult.getOrderedOccurrences().firstEntry().getKey(), counterResult.getOrderedOccurrences().firstEntry().getValue()));
            outputWriter.writeFileFromList(DICT_FILE_SUFFIX + currentLetter + DICT_FILE_EXTENSION, outDir, currentGroup);
        } else {
            for(Map.Entry<String, Integer> e : counterResult.getOrderedOccurrences().entrySet()) {
                char currentLetter = e.getKey().charAt(0);
                //If last
                if (index == counterResult.getOrderedOccurrences().size() - 1) {
                    currentGroup.add(new WordCount(e.getKey(), e.getValue()));
                    outputWriter.writeFileFromList(DICT_FILE_SUFFIX + lastLetter + DICT_FILE_EXTENSION, outDir, currentGroup);
                }
                //If first
                if(index == 0) {
                    lastLetter = currentLetter;
                    currentGroup.add(new WordCount(e.getKey(), e.getValue()));
                    index++;
                } else if(lastLetter != currentLetter) {
                    outputWriter.writeFileFromList(DICT_FILE_SUFFIX + lastLetter + DICT_FILE_EXTENSION, outDir, currentGroup);
                    lastLetter = currentLetter;
                    currentGroup = new ArrayList<>();
                    currentGroup.add(new WordCount(e.getKey(), e.getValue()));
                    index++;
                } else if (index != counterResult.getOrderedOccurrences().size()) {
                    currentGroup.add(new WordCount(e.getKey(), e.getValue()));
                    index++;
                }
            }

        }
    }
}
