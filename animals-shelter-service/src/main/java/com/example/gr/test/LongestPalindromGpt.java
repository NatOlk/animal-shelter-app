package com.example.gr.test;


public class LongestPalindromGpt {

    public static void main(String[] args) {
        String checkPalindrome = "dffd";
        System.out.println("RESULT = " + longestPalindrome(checkPalindrome));
    }

    public static String longestPalindrome(String s) {

        if (s == null || s.length() < 1) return "";
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            System.out.println("Length expand i i " + len1);
            int len2 = expandAroundCenter(s, i, i + 1);
            System.out.println("Length expand i i + 1 " + len1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        System.out.println("S = " + s);
        System.out.println(" l = " + left);
        System.out.println(" r = " + right);
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            System.out.println("we are here");
            left--;
            right++;
        }
        return right - left - 1;
    }
}

