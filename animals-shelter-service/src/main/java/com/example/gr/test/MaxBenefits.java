package com.example.gr.test;


import java.util.Arrays;

public class MaxBenefits {

    public static void main(String[] args) {
        int [] nums = { 1, 9, 4, 5, 8};

        System.out.println(maxBenefits(nums));
    }

    public static int maxBenefits(int[] nums) {

        if (nums.length == 0) return 0;
        if(nums.length == 1) return nums[0];
        return Arrays.stream(nums).max().getAsInt() - Arrays.stream(nums).min().getAsInt();
    }
}

