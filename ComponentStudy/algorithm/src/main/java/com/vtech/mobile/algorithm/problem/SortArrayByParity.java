package com.vtech.mobile.algorithm.problem;

import java.util.Arrays;

public class SortArrayByParity {

    public static void main(String[] args) {
        int[] arr = {3, 1, 4, 2,6,6};
        sortArrayByParity(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 给定一个整数数组 nums,将nums中所有偶数元素移动到数组的前面，后面根所有奇数元素
     * 思路：
     * 从数组两端同时遍历，左端指针遇到奇数与右端指针位置交换，右端指针位置遇到偶数与左端指针位置交换，奇偶符合要求则指针向中间移动，左右指针相遇则排序完成
     */
    public static void sortArrayByParity(int[] nums) {
        if (nums == null || nums.length < 1) {
            return;
        }
        int left = 0;
        int right = nums.length - 1;
        while (left < nums.length && right >= 0 && left!=right) {
            // 左边遇到奇数，和右端指针位置交换
            if (nums[left] % 2 == 1) {
                int tmp = nums[right];
                nums[right] = nums[left];
                nums[left] = tmp;
                right--;
            } else {
                left++;
            }
        }
    }
}
