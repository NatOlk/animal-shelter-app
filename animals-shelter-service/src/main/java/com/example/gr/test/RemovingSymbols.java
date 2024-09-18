package com.example.gr.test;

public class RemovingSymbols {

    public static void main(String[] args) {
        String checkString = "ABBAAAB";
        System.out.println(removeSubstringsAndCount(checkString));
    }

    public static int removeSubstringsAndCount(String s) {

        while (s.contains("AB")) {
            s = s.replace("AB", "");
        }

        while (s.contains("BB")) {
            s = s.replace("BB", "");
        }

        return s.length();
    }
}

