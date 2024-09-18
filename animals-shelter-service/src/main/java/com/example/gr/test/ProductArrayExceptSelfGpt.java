package com.example.gr.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ProductArrayExceptSelfGpt {

    public static void main(String[] args) {
        int [] nums = {2, 3, 4};

        System.out.println(Arrays.toString(productExceptSelf(nums)));
    }

    public static int[] productExceptSelf(int[] nums) {

        int n = nums.length;

        int[] leftProducts = new int[n];
        int[] rightProducts = new int[n];

        leftProducts[0] = 1;
        for (int i = 1; i < n; i++) {

            leftProducts[i] = leftProducts[i - 1] * nums[i - 1];
            System.out.println("i " + i + " lp " + leftProducts[i]);
        }

        rightProducts[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {

            rightProducts[i] = rightProducts[i + 1] * nums[i + 1];
            System.out.println("i " + i + " rp " + rightProducts[i]);
        }

        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = leftProducts[i] * rightProducts[i];
        }

        return result;
    }
}


