package com.vtech.mobile.algorithm.problem;

public class Palindrome {

    public static void main(String[] args) {

        String str = "abc12dc21cba";
        System.out.println(palindrome(str, 0));

        System.out.println(4&4);
    }

    public static boolean palindrome(String str, int index) {
        if (str.charAt(index) == str.charAt(str.length() - 1 - index)) {
            if (index == (str.length() - 1) / 2) {
                return true;
            }
            return palindrome(str, index + 1);
        } else {
            return false;
        }
    }
}
