package com.example.gr.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProductArrayExceptSelf {

    public static void main(String[] args) {
        int [] nums = {1,2,3,4};

        System.out.println(Arrays.toString(productExceptSelf(nums)));
    }

    public static int[] productExceptSelf(int[] nums) {

        List<Long> productNums = new ArrayList<>(nums.length);

        for(int i = 0; i < nums.length; i++) {
            int finalI = i;
            productNums.add(IntStream.range(0, nums.length)
                    .filter(j -> j != finalI)
                    .mapToLong(j -> nums[j])
                    .reduce(1, (a, b) -> a * b));
        }
        return productNums.stream().mapToInt(Long::intValue).toArray();
    }
}

