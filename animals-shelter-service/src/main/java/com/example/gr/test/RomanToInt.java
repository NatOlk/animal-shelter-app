package com.example.gr.test;


import java.util.*;

public class RomanToInt {

    public static void main(String[] args) {
        String roman = "XIV";

        System.out.println(romanToInt(roman));
    }
    public static int romanToInt(String s) {

        int sum = 0;
        Map<Character, Integer> romanNumbers = new HashMap<>();
        romanNumbers.put('I', 1);
        romanNumbers.put('V', 5);
        romanNumbers.put('X', 10);
        romanNumbers.put('L', 50);
        romanNumbers.put('C', 100);
        romanNumbers.put('D', 500);
        romanNumbers.put('M', 1000);
        int previousNumber = 0;

        for (int i = s.length() -1; i >= 0 ; i--) {
            char currentNumber = s.charAt(i);

            Integer number = romanNumbers.get(currentNumber);
            if (number >= previousNumber) {
                sum+=number;
            } else {
                sum -= number;
            }
            previousNumber = number;

        }
        return sum;
    }
}

