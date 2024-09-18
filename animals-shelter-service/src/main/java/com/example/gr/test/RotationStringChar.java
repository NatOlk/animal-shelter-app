package com.example.gr.test;

public class RotationStringChar {

    public static void main(String[] args) {
        String checkString = "absmns";
        String rotation = "a";
        System.out.println(rotateString(checkString));
    }

    public static String rotateString(String checkString) {

      /*char [] arr =  checkString.toCharArray();

      for (int i = 0; i < arr.length / 2; i++) {
          char l = arr[l];
      }*/

      char [] a = new char[checkString.length()];
      for (int i = 0; i < checkString.length(); i++) {
          a[checkString.length() - i - 1] = checkString.charAt(i);
      }

      return new String(a);
    }

    private static boolean rotate(String checkString, int rotationIndex, String rotation)
    {
        int length = checkString.length();
        return checkString.substring(rotationIndex).equals(rotation.substring(0, length - rotationIndex))
                && checkString.substring(0, rotationIndex).equals(rotation.substring(length - rotationIndex));
    }
}

