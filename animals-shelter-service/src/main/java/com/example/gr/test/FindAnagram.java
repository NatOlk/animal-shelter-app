package com.example.gr.test;


public class FindAnagram {

    public static void main(String[] args) {
        String a = "baabDasdfvsdfR";
        String b = "p";

        System.out.println(findAnagram(a, b));
    }

    private static int findAnagram (String a, String b) {

        char [] aChss = a.toCharArray();
        char [] bChss = b.toCharArray();

        int aSz = a.length();
        int bSz = b.length();

        if (bSz > aSz) return -1;

        for (int i = 0 ; i < aSz; i++) {

            int k = 0;
            for (int j = 0; j < bSz; j++) {
                if (aChss[i + k] != bChss[j]) {
                    break;
                }
                k++;
                if (j == bSz - 1) return i;
            }
        }
        return -1;
    }
}

