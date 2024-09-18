package com.example.gr.test;


import java.util.Arrays;

public class NumbersSumTarget {

    public static void main(String[] args) {
        int [] nums = {1, 3, 2, 7, 1, 4};

        System.out.println(Arrays.toString(twoSum(nums, 10)));
    }

    public static int[] twoSum(int[] nums, int target) {

        for (int i = 0; i < nums.length; i++) {
            int sum = nums[i];
            System.out.println("Sum 1 = " + sum);
            for(int j = i + 1; j < nums.length; j++) {
                sum += nums[j];
                System.out.println("Sum 2 = " + sum);
                if (sum == target) {
                    return Arrays.copyOfRange(nums, i, j + 1);
                }
            }
        }
        return new int [] {};
    }
}

