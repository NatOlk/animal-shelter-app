package com.example.gr.test;


public class StringEncode {

    public static void main(String[] args) {
        String str = "aabbbsstt";

        System.out.println(stringEncode(str));
    }

    private static String stringEncode(String s) {

        StringBuilder sb = new StringBuilder();
        char sym = s.charAt(0);
        int symLength = 1;

        for (int i = 1; i < s.length(); i++) {
            System.out.println("i = " + i);
            System.out.println("Sym " + sym);

            if (s.charAt(i) == sym) {
                symLength++;
            } else {
                sb.append(sym).append(symLength);
                sym = s.charAt(i);
                symLength = 1;
            }
            if (i == s.length() - 1) {
                sb.append(sym).append(symLength);
            }
            System.out.println("_______________________________");
        }
        return sb.toString();
    }
}

