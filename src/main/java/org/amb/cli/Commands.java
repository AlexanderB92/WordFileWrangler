package org.amb.cli;

import org.amb.io.OutputWriter;
import org.amb.model.CounterResult;
import org.amb.tools.Counters;
import org.amb.util.LoremFileGenerator;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Commands {

    //CLI Commands
    public void generateFiles(int filecount, int wordcount, String filename) {
        LoremFileGenerator w = new LoremFileGenerator(filecount, wordcount, filename);
        try {
            w.generateTextFiles();
            System.out.printf("Created %1$s files with %2$s words and named %3$s", filecount, wordcount, filename);
        } catch(Exception e) {
            System.out.println("File generation unsuccesful due to error: \n" + e.getMessage() + "see error log for details");
            //todo: Error log
        }
    }

    public void countOccurrences(String dir) {
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInDirectory(dir);
        printOccurrences(cr.getOccurences());
    }

    public void countExcluded(String dir) throws IOException {
        Counters r = new Counters();
        int i = r.countExcludedWords(dir);
        OutputWriter outputWriter = new OutputWriter();
        outputWriter.saveSimpleString("excluded_result",dir,String.valueOf(i));
        System.out.println("Excluded words: " + i);
        System.out.println("Result saved to " + dir);
    }

    public void createDictionaryLikeFiles(String dir) throws IOException {
        Counters r = new Counters();
        CounterResult cr = r.countOccurrencesInDirectory(dir);
        int written = r.createFileDictionaryLike(cr, dir);
        System.out.println(written + " files written to " + dir + "result_dict");
    }


    //CLI MAIN

    public void initCLI(String[] args ) throws ParseException, IOException {
        Options options = createOptions(args);
        parseArgs(args, options);
    }

    public Options createOptions(String[] args) {
        Options options = new Options();

        //help
        Option help = Option.builder("h")
                .longOpt("help")
                .desc("Prints help")
                .build();

        options.addOption(help);


        //generate files
        Option generateFiles = Option.builder("generate")
                .longOpt("generate")
                .argName("filecount,wordcount,filename")
                .hasArgs()
                .numberOfArgs(3)
                .desc("Generates files based on given parameters")
                .build();

        //count occurences
        Option countOccurrences = Option.builder("countoccurrences")
                .longOpt("countoccurrences")
                .argName("path")
                .hasArg()
                .desc("Counter the number of occurences for each word from all files")
                .build();

        //count occurences
        Option countExcludedWords = Option.builder("countexcluded")
                .longOpt("countexcluded")
                .argName("path")
                .hasArg()
                .desc("Counter the number of excluded words total (assumed from requirement) and save to file")
                .build();

        //create Dictionary Like Files
        Option createDictionaryFiles = Option.builder("createdict")
                .longOpt("createdict")
                .argName("path")
                .hasArg()
                .desc("Create a file for each unique first letter and sort words by first letter into same files")
                .build();

        options.addOption(generateFiles);
        options.addOption(countOccurrences);
        options.addOption(countExcludedWords);
        options.addOption(createDictionaryFiles);

        //Help
        if(args.length == 0) {
            printHelp(options);
        }

        return options;
    }

    public void parseArgs(String[] args, Options options) throws ParseException, IOException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption('h')) {
            printHelp(options);
        }

        if(cmd.hasOption("generate")) {
            String[] values = cmd.getOptionValues("generate");
            int filecount = Integer.parseInt(values[0]);
            int wordcount = Integer.parseInt(values[1]);
            generateFiles(filecount, wordcount, values[2]);
        }

        if(cmd.hasOption("countoccurrences")) {
            String[] values = cmd.getOptionValues("countoccurrences");
            String path = values[0];
            countOccurrences(path);
        }

        if(cmd.hasOption("countexcluded")) {
            String[] values = cmd.getOptionValues("countexcluded");
            String path = values[0];
            countExcluded(path);
        }
        if(cmd.hasOption("createdict")) {
            String[] values = cmd.getOptionValues("createdict");
            String path = values[0];
            createDictionaryLikeFiles(path);
        }
    }

    private void printOccurrences(HashMap<String, Integer> occurences) {
        for(Map.Entry<String, Integer> e : occurences.entrySet()) {
            System.out.println("Word: " + e.getKey() + ", count: " + e.getValue());
        }
    }

    private void printHelp(Options options) {
        System.out.println("usage: ");
        for(Option o : options.getOptions()) {
            if(o.getLongOpt() != "help") {
                System.out.print("[-" + o.getLongOpt() + " <" + o.getArgName().replace(',',' ') + ">]");
            }
        }
    }
}
