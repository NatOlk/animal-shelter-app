package com.example.gr.test;


public class LongestCommentPrefix {

    public static void main(String[] args) {
        String st1 = "ab";
        String st2 = "a";
        String st3 = "bf";
        String st4 = "bfsdf";

        System.out.println(longestCommonPrefix(new String[]{st1, st2}));
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs[0].length(); i++) {
            char cc = strs[0].charAt(i);
            for (String str : strs) {
                System.out.println("str " + str);
                if (str.length() <= i || str.charAt(i) != cc) {
                    return sb.toString();
                }
            }
            sb.append(cc);
        }
        return sb.toString();
    }
}

