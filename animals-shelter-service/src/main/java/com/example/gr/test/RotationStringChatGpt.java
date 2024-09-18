package com.example.gr.test;

public class RotationStringChatGpt {

    public static void main(String[] args) {
        String checkString = "abcde";
        String rotation = "cdeab";
        System.out.println(rotateString(checkString, rotation));
    }

    public static boolean rotateString(String checkString, String rotation) {
        if (checkString.equals(rotation)) return true;

        int n = checkString.length();
        if (n != rotation.length()) return false;

        for (int i = 1; i < n; i++) {
            System.out.println("------");
            if (checkRotation(checkString, rotation, i)) return true;
        }

        return false;
    }

    private static boolean checkRotation(String checkString, String rotation, int rotationIndex) {
        int n = checkString.length();
        System.out.println("Rotation = " + rotation);
        System.out.println("Init = " + checkString);
        System.out.println("RotationIndex = " + rotationIndex);
        System.out.println("n = " + n);
        for (int i = 0; i < n; i++) {
            System.out.println("I = " + i);
            System.out.println("I + rotationIndex = " + (i + rotationIndex));
            System.out.println("I + rotationIndex % n = " + (i + rotationIndex) % n);
            System.out.println("checkString.charAt((i + rotationIndex) % n)  = " + checkString.charAt((i + rotationIndex) % n) );
            System.out.println("rotation.charAt(i)  = " + rotation.charAt(i));
            if (checkString.charAt((i + rotationIndex) % n) != rotation.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}

