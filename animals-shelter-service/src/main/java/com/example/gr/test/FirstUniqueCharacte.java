package com.example.gr.test;


import java.util.HashSet;
import java.util.Set;

public class FirstUniqueCharacte {

    public static void main(String[] args) {
        String un = "lbaabl";

        System.out.println(firstUniqChar(un));
    }

    public static int firstUniqChar(String s) {

        Set<Character> trackingChar = new HashSet<>();
       for (int i = 0; i < s.length(); i++) {
           char ch = s.charAt(i);
           int indexNext = s.indexOf(ch, i + 1);
           if (indexNext == -1 && !trackingChar.contains(ch)) return i;
           trackingChar.add(ch);
       }

        return -1;
    }
}

