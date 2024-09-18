package com.example.gr.test;


import java.util.UUID;

public class RemoveDuplicates {

    public static void main(String[] args) {
        String stre = "abbbabaaa";

        System.out.println("result = " + removeDuplicates(stre));
    }

    public static String removeDuplicates(String s) {
       String initial = s;
        while(true) {
           String removeDup = removeDup(initial);
           System.out.println("removed dup = " + removeDup);
            System.out.println("initial = " + initial);
           if (removeDup.equals(initial)) return removeDup;
           initial = removeDup;
        }
    }

    private static String removeDup(String s) {
        StringBuilder sb = new StringBuilder(s);
        System.out.println("s = " + s);
        if (s.length() == 1) return s;
        if (s.length() == 2) {
            if (s.charAt(0) == s.charAt(1)) {
                return "";
            }
            return s;
        };

        for(int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                sb.delete(i, i + 2);
                break;
            }
        }
        return sb.toString();
    }
}

