package com.vtech.mobile.algorithm.recursion;

public class Demo01 {

    public static void main(String[] args) {
        int[] arrInt = {2, 34, 1, 0, 0, 4, 10, 10};
        System.out.println(process(arrInt, 0, arrInt.length - 1));
    }

    // 二分查找 数组中最大的数
    public static int process(int[] arr, int L, int R) {
        if (L == R) {
            return arr[L];
        }
        int mid = L + ((R - L) >> 1);
        int leftMax = process(arr, L, mid);
        int rightMax = process(arr, mid + 1, R);
        return Math.max(leftMax, rightMax);
    }
}
