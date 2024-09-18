package com.example.gr.test;


import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;

public class LongestSequence {

    public static void main(String[] args) {

        String income = "4";
        System.out.println(longestSequence(income));
    }

    private static String longestSequence (String income) {

        if (Strings.isEmpty(income)) return "";
        Map<String, Integer> symbolsLength = new HashMap<>();

        for (int i = 0; i < income.length(); i++)
        {
            String sym = String.valueOf(income.charAt(i));
            Integer storedSymLength = symbolsLength.get(sym);
            if (i == 0) {
               symbolsLength.put(sym, 1);
            } else {
                if (income.charAt(i) == income.charAt(i - 1)) {
                    symbolsLength.merge(sym, 1, Integer::sum);
                } else {
                    if (storedSymLength == null) {
                        symbolsLength.put(sym, 1);
                    }
                }
            }
        }
        int maxSeq = symbolsLength.values().stream()
                .max(Integer::compareTo).orElse(1);

        String repeatedSymbol = symbolsLength.keySet().stream()
                .filter(key -> symbolsLength.get(key).equals(maxSeq))
                .findFirst().get();
        return repeatedSymbol.repeat(maxSeq);
    }
}

