package com.example.gr.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NTest2 {

    public static void main(String[] args) {

       int [] nums = {2, 10, 5, 2, 2, 4, 5, 2, 2, 5, 5, 5};
       System.out.println(majorityElement(nums));

    }
    public static List<Integer> majorityElement(int[] nums) {

        int max = nums.length / 3;
        Map<Integer, Integer> numberCount = new HashMap<>();
        for (int i = 0; i < nums.length;i++)
        {
            numberCount.compute(nums[i], (n, v) -> (v == null) ? 1 : v + 1);
        }
        System.out.println(numberCount);
        return numberCount.keySet().stream()
                .filter(k -> numberCount.get(k) > max)
                .collect(Collectors.toList());
    }
}

