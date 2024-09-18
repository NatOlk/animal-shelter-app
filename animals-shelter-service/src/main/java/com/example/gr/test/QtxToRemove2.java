package com.example.gr.test;


public class QtxToRemove2 {

    public static void main(String[] args) {
        String stre = "aaaabaaaabbbbabbbb";

        System.out.println(getQtxToRemove(stre));
    }

    private static int getQtxToRemove (String stre) {
        if (stre ==  null || stre.isEmpty()) return -1;

        int sz = stre.length();

        int bIndex = stre.lastIndexOf('a', sz);
        int bIndexLast = stre.lastIndexOf('b', sz);

        if (bIndexLast != sz - 1 || bIndex == -1) return -1;

        int startIndex = stre.lastIndexOf('a', stre.indexOf('b'));
        int numA = 0;
        int numB = 0;

        for (int i = startIndex + 1;i < bIndex + 1; i++) {
            if (stre.charAt(i) == 'a') numA++;
            if (stre.charAt(i) == 'b') numB++;
        }

        return Math.min(numA, numB);
    }

}

