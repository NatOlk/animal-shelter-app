package com.example.gr.test;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSumGpt {

    public static void main(String[] args) {
        int [] nums = {1, 1, 2, 7, 3, 4};

        System.out.println(Arrays.toString(twoSum(nums, 4)));
    }

    public static int[] twoSum(int[] nums, int target) {

        Map<Integer, Integer> map = new HashMap<>();


        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {

                return new int[] { map.get(complement), i };
            }

            map.put(nums[i], i);
        }


        return new int[] {};
    }
}

