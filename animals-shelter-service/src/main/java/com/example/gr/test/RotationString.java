package com.example.gr.test;

public class RotationString {

    public static void main(String[] args) {
        String checkString = "aabdc";
        String rotation = "abdca";
        System.out.println(rotateString(checkString, rotation));
    }

    public static boolean rotateString(String checkString, String rotation) {

        if (checkString.equals(rotation)) return true;
        if (checkString.length() != rotation.length()) return false;
        char firstRS = rotation.charAt(0);
        int indexFSInCheck = checkString.indexOf(firstRS, 0);

        while (indexFSInCheck != -1)
        {
            if (rotate(checkString, indexFSInCheck, rotation)) return true;
            indexFSInCheck = checkString.indexOf(firstRS, indexFSInCheck + 1);
        }
        return false;
    }

    private static boolean rotate(String checkString, int rotationIndex, String rotation)
    {
        int length = checkString.length();
        return checkString.substring(rotationIndex).equals(rotation.substring(0, length - rotationIndex))
                && checkString.substring(0, rotationIndex).equals(rotation.substring(length - rotationIndex));
    }
}

