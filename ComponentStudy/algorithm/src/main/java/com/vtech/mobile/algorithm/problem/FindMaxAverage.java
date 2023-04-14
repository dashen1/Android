package com.vtech.mobile.algorithm.problem;

public class FindMaxAverage {

    public static void main(String[] args) {

        int[] arr = {1,12,-5,-6,50,3};
        System.out.println(findMaxAverage(arr,4));
    }

    public static double findMaxAverage(int[] nums, int k) {
        int len = nums.length;
        int maxValue = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        maxValue = sum;
        for (int i = k; i < len; i++) {
            sum = sum - nums[i - k] + nums[i];
            if (sum > maxValue) {
                maxValue = sum;
            }
        }
        return 1.0 * maxValue / k;
    }
}
