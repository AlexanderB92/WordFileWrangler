package org.amb;

import org.amb.model.CounterResult;
import org.amb.tools.Counters;
import org.amb.io.DirectoryReader;
import org.amb.util.LoremFileGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * Unit test for WordFileWrangler.
 */
public class ApplicationUnitTest {

    private final String GENERATED_DATA_DIR = Paths.get("").toAbsolutePath() + File.separator + "testdata" + File.separator + "generated_data";
    private final String STATIC_DATA_DIR = Paths.get("").toAbsolutePath() + File.separator + "testdata" + File.separator + "static_data";
    private final String COUNTER_OUT_DIR = Paths.get("").toAbsolutePath() + File.separator + "testdata" + File.separator + "result_data";
    private final String ORDERED_STATIC_DATA_DIR = Paths.get("").toAbsolutePath() + File.separator + "testdata" + File.separator + "ordered_static_data";
    private final String ORDERED_COUNTER_OUT_DIR = Paths.get("").toAbsolutePath() + File.separator + "testdata" + File.separator + "result_data_from_ordered";


    @BeforeAll
    public static void createTestData() throws IOException {
        //Create random testfiles
        LoremFileGenerator w = new LoremFileGenerator(2, 200, "testfile");
        w.generateTextFiles();
    }


    /**
     * Tests if directory contents is read correclty
     * @throws IOException
     */
    @Test
    public void testGetFileNamesForDirectory() {

        //Setup: Instantiate reader and call method
        DirectoryReader r = new DirectoryReader();
        ArrayList<String> txtfiles = r.getDirectoryFileNames(GENERATED_DATA_DIR);

        //Assume: Test data files are found
        Assertions.assertTrue(txtfiles.contains("testfile1.txt"));
        Assertions.assertTrue(txtfiles.contains("testfile2.txt"));
    }


    /**
     * Checks specific words are counted correctly in 1 file
     * @throws IOException
     */
    @Test
    public void testCountOccurrencesForStaticDataSingleFile() {

        //Setup: Instantiate reader and call method
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInFile(STATIC_DATA_DIR + "\\randomwords_1.txt");

        //Assume: ie. egestas matches dataset (2)
        Assertions.assertEquals(2, cr.getOccurences().get("egestas"));
        Assertions.assertEquals(2, cr.getOccurences().get("felis"));
        Assertions.assertEquals(3, cr.getOccurences().get("et"));
        Assertions.assertEquals(6, cr.getOccurences().get("nulla"));

    }

    /**
     * Checks specific words are counted correctly across multiple files in directory
     * @throws IOException
     */
    @Test
    public void testCountOccurrencesForStaticDataDirectory() {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInDirectory(STATIC_DATA_DIR);

        //Assume: ie. egestas matches dataset (8)
        Assertions.assertEquals(8, cr.getOccurences().get("egestas"));
        Assertions.assertEquals(4, cr.getOccurences().get("felis"));
        Assertions.assertEquals(10, cr.getOccurences().get("et"));
        Assertions.assertEquals(8, cr.getOccurences().get("nulla"));
        Assertions.assertEquals(5, cr.getOccurences().get("nullam"));
        Assertions.assertEquals(15, cr.getOccurences().get("ipsum"));
        Assertions.assertEquals(7, cr.getOccurences().get("eget"));
    }


    /**
     * Checks if excluded words are read correctly when counting all occurrences
     * @throws IOException
     */
    @Test
    public void testCountOccurencesAndExcludedStaticTest() {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInDirectory(STATIC_DATA_DIR);

        //Assume: ie. egestas matches dataset (8)
        Assertions.assertEquals(80, cr.getExclusions());
    }


    /**
     * Tests if excluded word counters matches test data
     * @throws IOException
     */
    @Test
    public void testCountExcludedOnlyStaticTest() {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        int excluded = r.countExcludedWords(STATIC_DATA_DIR);

        //Assume: ie. egestas matches dataset (8)
        Assertions.assertEquals(80, excluded);
    }


    /**
     * Checks for existence of example word
     * @throws IOException
     */
    @Test
    public void testWordActualExcluded() {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInDirectory(STATIC_DATA_DIR);


        //Assume: justo must not be included in result, although occurence actually is:
        Assertions.assertNull(cr.getOccurences().get("justo"));

    }

    /**
     * Checks if excluded words are read correctly
     * @throws IOException
     */
    @Test
    public void testExclusionReaderUtil() {
        //Setup: Instantiate reader and call method
        DirectoryReader r = new DirectoryReader();
        HashSet<String> result = r.getExclusions(STATIC_DATA_DIR);

        //Assume: test all 10 values
        Assertions.assertTrue(result.contains("diam"));
        Assertions.assertTrue(result.contains("neque"));
        Assertions.assertTrue(result.contains("consectetur"));
        Assertions.assertTrue(result.contains("dapibus"));
        Assertions.assertTrue(result.contains("tellus"));
        Assertions.assertTrue(result.contains("tincidunt"));
        Assertions.assertTrue(result.contains("sem"));
        Assertions.assertTrue(result.contains("pretium"));
        Assertions.assertTrue(result.contains("eleifend"));
        Assertions.assertTrue(result.contains("justo"));

        Assertions.assertEquals(10, result.size());
    }

    /**
     * Checks if file is created as expected in the correct dir when using the unordered method
     * @throws IOException
     */
    @Test
    public void testCreateDictionaryUnorderedForStaticDataDirectoryFile() throws IOException, InterruptedException {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInDirectory(STATIC_DATA_DIR);

        //Use result to call dictionary-like file creator
        r.createFileDictionaryLike(cr, COUNTER_OUT_DIR);

        //Assume file created (could also assert number of files)
        //todo: WatchService or similar to watch for filecreation

    }

    /**
     * Checks if file is created as expected in the correct dir
     * @throws IOException
     */
    @Test
    public void testCreateDictionaryOrderedForStaticDataOrderedDirectoryFile() throws IOException {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInDirectoryOrdered(ORDERED_STATIC_DATA_DIR);

        //Use result to call dictionary-like file creator
        r.createFileDictionaryLikeFromOrderedList(cr, ORDERED_COUNTER_OUT_DIR);

        //Assume file created (could also assert number of files)
        Path resultFile = Paths.get(ORDERED_COUNTER_OUT_DIR + "\\FILE_a.txt");
        Assertions.assertTrue(Files.exists(resultFile));
    }

    /**
     * Measures performance for counting occurences and creating dictionary files with unordered data
     * @throws IOException
     */
    @Test
    public void testCreateDictionarySlowForOrderedDataDirectoryFile() throws IOException {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        long startTime = System.nanoTime();
        CounterResult cr = r.countOccurrencesInDirectory(ORDERED_STATIC_DATA_DIR);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Ordered data - (hashmap) approach. Time to count all occurrences: " + duration / 1000000 + " ms");


        //Use result to call dictionary-like file creator
        startTime = System.nanoTime();
        r.createFileDictionaryLike(cr, COUNTER_OUT_DIR);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.println("Ordered data - (hashmap) approach. Time to write files: " + duration / 1000000 + " ms");
        //Some assertion about the files

    }

    /**
     * Measures performance for counting occurences and creating dictionary files with pre ordered data
     * @throws IOException
     */
    @Test
    public void testCreateDictionaryFastForOrderedDataDirectoryFile() throws IOException {

        //Setup: Instantiate counter and call method
        Counters r = new Counters();
        long startTime = System.nanoTime();
        CounterResult cr = r.countOccurrencesInDirectoryOrdered(ORDERED_STATIC_DATA_DIR);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Ordered data - ordered (treemap) approach. Time to count all occurences: " + duration / 1000000 + " ms");


        //Use result to call dictionary-like file creator
        startTime = System.nanoTime();
        r.createFileDictionaryLikeFromOrderedList(cr, COUNTER_OUT_DIR);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.println("Ordered data - ordered (treemap) approach. Time to write files: " + duration / 1000000 + " ms");
        //Some assertion about the files

    }
}
