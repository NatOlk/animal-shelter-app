package com.example.gr.test;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CheckLowUpperCapitals {

    public static void main(String[] args) {
        String stre = "aAbBcC";

        System.out.println(checkLowUpperCapitals(stre));
    }

    private static long checkLowUpperCapitals (String stre) {

        Map<Character,Character> uniqueChars = new HashMap<>();

        for(int i = 0 ; i < stre.length(); i++) {
            Character ch = stre.charAt(i);
            boolean isLow = Character.isLowerCase(ch);
            Character chLow = Character.toLowerCase(ch);
           if (isLow) {
               uniqueChars.put(ch, null);
               System.out.println("l " + uniqueChars);
           } else {
               System.out.println("ch low " + uniqueChars.get(chLow));
               uniqueChars.putIfAbsent(chLow, ch);
               System.out.println("u " + uniqueChars);
           }
        }
        return uniqueChars.values()
                .stream()
                .filter(Objects::nonNull)
                .count();
    }
}

