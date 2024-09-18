package com.example.gr.test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class NTestAppearTwice {

    public static void main(String[] args) throws IOException {

      // int [] nums = {1, 2,6, 8};
       String filePath = "C:\\gr\\src\\main\\java\\com\\example\\gr\\5.txt";
       int [] nums = readIntArrayFromFile(filePath);
       System.out.println(containsDuplicate(nums));

    }
    public static boolean containsDuplicate(int[] nums)
    {
        Arrays.sort(nums);
        return IntStream.range(0, nums.length)
                .anyMatch(i -> {if (i != nums.length - 1) { return nums[i] == nums[i + 1];} else return false;});
    }

    public static int [] readIntArrayFromFile(String filePath) throws IOException {
        List<Integer> numberList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.startsWith("[") && line.endsWith("]")) {

                    String[] tokens = line.substring(1, line.length() - 1).split("\\s*,\\s*");
                    for (String token : tokens) {
                       numberList.add(Integer.parseInt(token.trim()));
                    }
                    break;
                }
            }
        }
        int[] numbers = new int[numberList.size()];
        for (int i = 0; i < numberList.size(); i++) {
            numbers[i] = numberList.get(i);
        }
        return numbers;
    }
}

