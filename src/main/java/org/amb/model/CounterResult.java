package org.amb.model;

import java.util.HashMap;
import java.util.TreeMap;

public class CounterResult {

    private HashMap<String, Integer> occurences;
    private TreeMap<String, Integer> orderedOccurrences;
    private int exclusions;

    public CounterResult(HashMap<String, Integer> occurences, TreeMap<String, Integer> orderedOccurrences, int exclusions) {
        this.occurences = occurences;
        this.orderedOccurrences = orderedOccurrences;
        this.exclusions = exclusions;
    }

    public HashMap<String, Integer> getOccurences() {
        return occurences;
    }

    public void setOccurences(HashMap<String, Integer> occurences) {
        this.occurences = occurences;
    }

    public TreeMap<String, Integer> getOrderedOccurrences() {
        return orderedOccurrences;
    }

    public void setOrderedOccurrences(TreeMap<String, Integer> orderedOccurrences) {
        this.orderedOccurrences = orderedOccurrences;
    }

    public int getExclusions() {
        return exclusions;
    }

    public void setExclusions(int exclusions) {
        this.exclusions = exclusions;
    }

}
