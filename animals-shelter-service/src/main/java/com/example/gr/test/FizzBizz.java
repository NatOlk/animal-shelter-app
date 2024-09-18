package com.example.gr.test;


import org.apache.logging.log4j.util.Strings;

public class FizzBizz {

    public static void main(String[] args) {

        fizzBizz(15);
    }

    private static void fizzBizz (int n) {
        for(int i = 1 ; i <= n; i++)
        {
            String s = "";
            if (i % 3 == 0) {
               s += "Fizz";

            }
            if (i % 5 == 0) {
                s += "Buzz";
            }
            if (Strings.isEmpty(s)) s = String.valueOf(i);
            System.out.println(s);
        }
    }
}

