package com.vtech.mobile.algorithm.problem;

public class MinSubArrayLen {

    public static void main(String[] args) {
        int[] arrInt = {2,3,1,2,4,3};
        System.out.println(minSubArrayLen(7,arrInt));
    }

    /**
     * 长度最小子数组
     * 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的连续子数组。
     * 如果不存在符合条件的连续子数组，返回 0。
     * @param s
     * @param nums
     * @return
     */
    public static int minSubArrayLen(int s, int[] nums) {
        int left = 0;
        int right = -1;
        int sum = 0;
        int result = nums.length + 1;
        while (left < nums.length) { // 窗口的左边界在数组范围内，则循环继续
            if (right + 1 < nums.length && sum < s) {
                right++;
                sum += nums[right];
            } else {
                sum -= nums[left];
                left++;
            }
            if (sum >= s) {
                result = Math.min((right - left + 1), result);
            }
        }
        if (result == nums.length + 1) {
            return 0;
        }
        return result;
    }
}
