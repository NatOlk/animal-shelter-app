package com.example.gr.test;


public class LongestSequenceCorrect {

    public static void main(String[] args) {

        String income = "4fffffffffsdfffsdfwerw33333444444";
        System.out.println(longestSequence(income));
    }

    public static String longestSequence(String income) {
        if (income == null || income.isEmpty()) return "";

        char[] chars = income.toCharArray();
        int maxLength = 1;
        int currentLength = 1;
        char maxChar = chars[0];

        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == chars[i - 1]) {
                currentLength++;
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    maxChar = chars[i];
                }
            } else {
                currentLength = 1;
            }
        }

        return String.valueOf(maxChar).repeat(maxLength);
    }
}

