package com.example.gr.test;


import java.util.HashSet;
import java.util.Set;

public class LongestSubstringWithoutRepeatingCharacters {

    public static void main(String[] args) {

        System.out.println("Result = " + lengthOfLongestSubstring("abfddff"));
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        Set<Character> seen = new HashSet<>();
        int maxLength = 0, left = 0;

        for (int right = 0; right < s.length(); right++) {
            System.out.println("seen " + seen);
            while (seen.contains(s.charAt(right))) {
                System.out.println("Alarm left " + left + " removing " + s.charAt(left));
                seen.remove(s.charAt(left));
                left++;
            }
            seen.add(s.charAt(right));
            System.out.println("Max " + maxLength + "; r - l + 1 " + (right - left + 1));
            maxLength = Math.max(maxLength, right - left + 1);

            System.out.println("Max " + maxLength + "; right " + right + " ; left " + left);
        }

        return maxLength;
    }
}

