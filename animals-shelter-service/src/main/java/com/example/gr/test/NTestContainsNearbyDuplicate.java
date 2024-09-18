package com.example.gr.test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class NTestContainsNearbyDuplicate {

    public static void main(String[] args) throws IOException {

        //  int [] nums = {313,64,612,515,412,661,244,164,492,744,233,579,62,786,342,817,838,396,230,79,570,702,124,727,586,542,919,372,187,626,869,923,103,932,368,891,411,125,724,287,575,326,88,125,746,117,363,8,881,441,542,653,211,180,620,175,747,276,832,772,165,733,574,186,778,586,601,165,422,956,357,134,857,114,450,64,494,679,495,205,859,136,477,879,940,139,903,689,954,335,859,78,20};
        String filePath = "C:\\gr\\src\\main\\java\\com\\example\\gr\\7.txt";
        int[] nums = readIntArrayFromFile(filePath);
        System.out.println(containsNearbyDuplicate(nums, 2489));

    }

    public static boolean containsNearbyDuplicate(int[] nums, int k) {

        int[] dupls = findDuplicateNums(nums);

        if (dupls.length == 0) return false;

        return IntStream.range(0, dupls.length)
                .anyMatch(d -> isContainsNearbyDuplicate(dupls[d], nums, k));
    }

    private static boolean isContainsNearbyDuplicate(int dup, int[] nums, int k) {
        Map<Integer, Integer> dupsIndexes = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == dup) {
                dupsIndexes.put(i, dup);
                if (isLessThanCutLevel(dupsIndexes, k)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isLessThanCutLevel(Map<Integer, Integer> dupsIndexes, int k) {
        List<Integer> keys = new ArrayList<>(dupsIndexes.keySet());

        for (int key : keys) {
            int value = dupsIndexes.get(key);
            if (isLessThanCutLevel(key, value, k, dupsIndexes)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLessThanCutLevel(int index, int v, int cutLevel, Map<Integer, Integer> dupsIndexes) {
        return dupsIndexes.keySet()
                .stream()
                .anyMatch(k ->
                {
                    if (k != index) {
                        int vl = dupsIndexes.get(k);
                        return vl == v && Math.abs(k - index) <= cutLevel;
                    }
                    return false;
                });
    }

    public static int[] findDuplicateNums(int[] nums) {
        int[] dupsArray = new int[nums.length];

        System.arraycopy(nums, 0, dupsArray, 0, nums.length);

        Arrays.sort(dupsArray);
        return IntStream.range(0, dupsArray.length)
                .filter(i -> {
                    if (i != dupsArray.length - 1) {
                        return dupsArray[i] == dupsArray[i + 1];
                    } else return false;
                })
                .map(i -> dupsArray[i])
                .toArray();
    }

    public static int[] readIntArrayFromFile(String filePath) throws IOException {
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

