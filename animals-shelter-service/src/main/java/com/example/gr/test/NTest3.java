package com.example.gr.test;


public class NTest3 {

    public static void main(String[] args) {

       int [] nums = {2, 10, 5, 2, 2, 4, 5, 2, 2, 5, 5, 5};
       System.out.println(twoSum(nums, 7));

    }
    private static int[] twoSum(int[] numbers, int target) {
        int [] result = {};
        for(int i = 0; i < numbers.length; i++)
        {
            while (recursiveCheck(numbers[i], numbers[i + 1], target));
        }
        return result;
    }

    private static boolean recursiveCheck(int firstNum, int secondNum, int sum)
    {
        if (firstNum + secondNum == sum) return true;
        return false;
    }
}

