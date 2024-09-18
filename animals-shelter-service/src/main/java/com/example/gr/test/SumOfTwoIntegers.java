package com.example.gr.test;


import java.util.Arrays;

public class SumOfTwoIntegers {

    public static void main(String[] args) {


        System.out.println(getSum(16,24));
    }

    public static int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1; // Перенос
            System.out.println("carry " + carry);
            a = a ^ b; // Сумма без учета переноса
            System.out.println("a " + a);
            b = carry; // Новое значение для следующей итерации
            System.out.println("b " + b);
        }
        return a;
    }
}
