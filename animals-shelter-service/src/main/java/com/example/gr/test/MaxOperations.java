package com.example.gr.test;


public class MaxOperations {

    public static void main(String[] args) {
        int [] nums = {1, 3, 2, 7, 1, 4};

        System.out.println(maxOperations(nums));
    }

    public static int maxOperations(int[] nums) {

        if (nums.length < 3) return 0;
        
        for (int i = 0; i < nums.length; i++)
        {
          if(i + 2 >= nums.length) return i;
          if (nums[i] + nums[i + 1] != nums [i + 2]) {
             return i;
          }
        }
        return nums.length;
    }
}

