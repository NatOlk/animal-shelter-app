package com.example.gr.test;


import java.util.Arrays;

public class FirstLastElements {

    public static void main(String[] args) {
        int [] nums = {0,1,1,1,1,2,2,2,3,3,3,3,4,5,6,6,8,9,9,9};

        System.out.println(Arrays.toString(searchRange(nums, 0)));
    }

    public static  int[] searchRange(int[] nums, int target) {

        int [] result = new int [] {-1, -1};
        int len = nums.length;
        int cutLength = len / 2;
        if (len == 0) return result;
        if (len == 1 && nums[0] == target) return new int [] {0, 0};
        int start = 0;

        while(true) {
            result = checkRange(nums, result, start, cutLength, len, target);
            if (result[0] != -1 && result[1]  != -1) break;
            if (result[0] == -1 && result[1]  == -1) break;
            System.out.println(Arrays.toString(result));

            if (result[1] != -1)  {
                start = cutLength;
                cutLength += (len - cutLength + 1) / 2;
                System.out.println("S " + start + "cut:" + cutLength + ";l " + len);
                if (start >= len) break;
            } else {
               len = len / 2;
               cutLength -= cutLength / 2;
                if (cutLength >= len) break;
            }
            System.out.println("l " + len);
            if (len == 1) break;
        }
        if (result[1] != -1 && result[0] == -1) {
            result[0] = result[1];
        } else if (result[0] != -1 && result[1] == -1) {
            result[1] = result[0];
        }

        return result;
    }

    private static int [] checkRange (int[] nums, int[] result, int start, int cutLength, int len, int target) {

        System.out.println("Start " + start);
        System.out.println("Cut " + cutLength);
        System.out.println("Len " + len);
        for (int i = start; i < cutLength + 1; i++) {
            System.out.println("I " + i);
            if (nums[i] == target && result [0] == -1) result [0] = i;
            if (nums[len - 1 - i] == target && result [1] == -1) result [1] = len - i - 1;
        }
        return result;
    }
}

