package com.example.gr.test;


public class QtxToRemove {

    public static void main(String[] args) {
        String stre = "aaaabaaaabbbbabbbb";

        System.out.println(getQtxToRemove(stre));
    }

    private static int getQtxToRemove (String stre) {
        if (stre ==  null || stre.isEmpty()) return -1;
        int qtx = 0;
        int sz = stre.length();
        int bIndex = stre.lastIndexOf('b');
        if (bIndex == 0 || bIndex == -1) return -1;
        int diff = (sz - 1) - bIndex;

        qtx += diff;

        char [] arr = stre.toCharArray();
        int symbolFirst = 1;
        int symbolSecond = 0;
        int changeCount = 0;
        int startIndex = stre.lastIndexOf('a', stre.indexOf('b'));
        System.out.println(startIndex);
        for (int i = startIndex + 2;i < bIndex; i++) {
            char currentChar = arr[i];
            char previousChar = arr[i - 1];
            System.out.println("Current char: " + currentChar + "; previous char: " + previousChar);
            System.out.println("SymbolFirst: " + symbolFirst + "; symbolSecond: " + symbolSecond);
            if (currentChar != previousChar) {
                changeCount++;
            }
            if (currentChar != previousChar && changeCount == 2) {

                System.out.println("Time to check: " + symbolFirst + ":" + symbolSecond);

                qtx += Math.min(symbolFirst, symbolSecond);
                System.out.println("Qtx:" + qtx );
                symbolFirst = 1;
                symbolSecond = 0;
                changeCount = 0;
            } else {
               if (changeCount == 1) {
                   symbolSecond++;
               } else {
                   symbolFirst++;
               }
            }
        }
        return qtx;
    }

}

