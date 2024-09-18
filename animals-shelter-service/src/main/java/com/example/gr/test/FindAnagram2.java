package com.example.gr.test;


import java.util.HashMap;

public class FindAnagram2 {

    public static void main(String[] args) {
        String a = "baabDasdfdvsdfRdfdf";
        String b = "dfdf";

        System.out.println(findAnagram(a, b));
    }

    private static int findAnagram(String a, String b) {
        int aSz = a.length();
        int bSz = b.length();

        if (bSz > aSz) return -1;

        HashMap<Character, Integer> bMap = new HashMap<>();
        for (char c : b.toCharArray()) {
            bMap.put(c, bMap.getOrDefault(c, 0) + 1);
        }

        System.out.println("B map" + bMap);
        HashMap<Character, Integer> windowMap = new HashMap<>();
        for (int i = 0; i < bSz; i++) {
            char c = a.charAt(i);
            windowMap.put(c, windowMap.getOrDefault(c, 0) + 1);
        }

        System.out.println("windows map" + windowMap);
        if (windowMap.equals(bMap)) return 0;

        for (int i = bSz; i < aSz; i++) {
            System.out.println("i " + i);

            char addChar = a.charAt(i);
            windowMap.put(addChar, windowMap.getOrDefault(addChar, 0) + 1);
            System.out.println("windows map in cycle " + windowMap);

            char removeChar = a.charAt(i - bSz);

            System.out.println("remove char " + removeChar);
            if (windowMap.get(removeChar) == 1) {
                windowMap.remove(removeChar);
            } else {
                windowMap.put(removeChar, windowMap.get(removeChar) - 1);
            }

            if (windowMap.equals(bMap)) return i - bSz + 1;
        }

        return -1;
    }
}

