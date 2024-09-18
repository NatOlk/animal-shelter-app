package com.example.gr.test;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class LongestSubstringContainingSet {

    public static void main(String[] args) {

        Set<Character> sch = new HashSet<>();
        sch.add('a');
        sch.add('b');

        System.out.println("Result = " + lengthOfLongestSubstringContainingSet("aaabfddff", sch));
    }

    public static int lengthOfLongestSubstringContainingSet(String s, Set<Character> charSet) {
        if (s == null || s.isEmpty() || charSet == null || charSet.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> charCount = new HashMap<>();
        int maxLength = 0, left = 0, requiredChars = charSet.size(), formed = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            System.out.println("right char " + rightChar);
            if (charSet.contains(rightChar)) {
                System.out.println("charset contains char ");
                charCount.put(rightChar, charCount.getOrDefault(rightChar, 0) + 1);
                if (charCount.get(rightChar) == 1) {
                    System.out.println("formed++");
                    formed++;
                }

            }
            System.out.println("while formed == requiredChars " + (formed == requiredChars));
            while (formed == requiredChars) {
                System.out.println("left " + left);
                maxLength = Math.max(maxLength, right - left + 1);
                System.out.println("max length " + maxLength);
                char leftChar = s.charAt(left);
                System.out.println("left char " + leftChar);
                if (charSet.contains(leftChar)) {
                    System.out.println("charset contains left char");
                    charCount.put(leftChar, charCount.get(leftChar) - 1);
                    if (charCount.get(leftChar) == 0) {
                        System.out.println("formed--");
                        formed--;
                    }
                }
                left++;
                System.out.println("left++");
                System.out.println("_____________________________");
            }
        }

        return maxLength;
    }
}

