package com.example.gr.test;


import java.util.Arrays;

public class MaxEndingHere {

    public static void main(String[] args) {
        int [] nums = {1, 3, 2, 7, -1, 4, 4, 4, 4,5};

        System.out.println(maxSubArray(nums));
    }

    public static int maxSubArray(int[] nums) {
        // Инициализация
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];

        // Проходим через массив начиная со второго элемента
        for (int i = 1; i < nums.length; i++) {
            // Обновляем maxEndingHere
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            // Обновляем maxSoFar
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }

        return maxSoFar;
    }
}

