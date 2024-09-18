package com.example.gr.test;


public class MaximumProfit {

    public static void main(String[] args) {
        int [] nums = {9, 3, 2, 7, 1, 4};

        System.out.println(maximumProfit(nums));
    }

    public static int maximumProfit(int[] nums) {

        if (nums.length < 2) return 0;
        int firstDaysProfit = nums[1] - nums[0];
        int maxProfit = Math.max(firstDaysProfit, 0);
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                int possibleMaxProfite = nums[j] - nums[i];
                if (possibleMaxProfite > 0 && possibleMaxProfite > maxProfit) {
                    maxProfit = possibleMaxProfite;
                }
            }
        }
        return maxProfit;
    }
}

