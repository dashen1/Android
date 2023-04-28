package com.vtech.mobile.algorithm.problem02;

public class LongestSubsequence {

    public static void main(String[] args) {

    }

    public static void process1(int[] arr) {
        int[] dp = new int[arr.length];
        dp[0] = 1;
        dp[1] = arr[1] > arr[0] ? 2 : 1;
        for (int index = 0; index < dp.length; index++) {
            int maxIndex = -1;
            int maxDp = Integer.MAX_VALUE;
            for (int i = 0; i < index; i++) {
                if (arr[i] < arr[index]) {
                    if (dp[i] > maxDp) {
                        maxIndex = i;
                        maxDp = dp[i];
                    }
                }
            }
            dp[index] = maxIndex == -1 ? 1 : maxDp + 1;
        }
    }
}
