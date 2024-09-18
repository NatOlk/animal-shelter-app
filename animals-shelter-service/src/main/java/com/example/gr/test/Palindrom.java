package com.example.gr.test;


public class Palindrom {

    public static void main(String[] args) {
        String checkPalindrom = "baab";

        System.out.println(isPalindrom(checkPalindrom));
    }

    private static boolean isPalindrom (String checkPalindrom) {
        for(int i = 0 ; i < checkPalindrom.length(); i++)
        {
            char f = checkPalindrom.charAt(i);
            char l = checkPalindrom.charAt(checkPalindrom.length() - 1 - i);
            if ( f != l ) return false;
        }
        return true;
    }
}

