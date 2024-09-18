package com.example.gr.test;

import java.util.ArrayList;
import java.util.List;

public class GenerateParenthesis {

    public static void main(String[] args) {

        //["((()))","(()())","(())()","()(())","()()()"] 3
        //["()"] 1
        //["(())", "()()"] 2
        System.out.println(generateParenthesis(3));
    }

    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        generate(result, "", 0, 0, n);
        return result;
    }

    private static void generate(List<String> result, String current, int open, int close, int max) {
        System.out.println("Result: " + result + "; current: " + current + "; open: " + open +
                "; close: " + close + "; max = " + max);

        System.out.println("Length - " + current.length());
        System.out.println("Mx 2 - " + max * 2);
        if (current.length() == max * 2) {
            result.add(current);
            return;
        }

        if (open < max) {
            System.out.println("Open < max");
            generate(result, current + "(", open + 1, close, max);
        }
        if (close < open) {
            System.out.println("Close < open");
            generate(result, current + ")", open, close + 1, max);
        }
    }
}

