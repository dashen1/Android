package com.vtech.mobile.algorithm.sort;

import java.util.Arrays;

public class InsertSort {
    public static void main(String[] args) {
        int[] arrInt = {2, 34, 1, 0, 0, 4, 10, 10};
        insertSort(arrInt);
        System.out.println(Arrays.toString(arrInt));
    }

    private static void insertSort(int[] arr) {
        // 插入个数，从第二个开始往前插
        for (int i = 1; i < arr.length; i++) {
            // 和前面要比较的个数
            for (int pre = i - 1; pre >= 0 && arr[pre] > arr[pre + 1]; pre--) {
                swap(arr, pre, pre + 1);
            }
        }
    }

    public static void swap(int[] arr, int indexL, int indexR) {
        int tmp = arr[indexR];
        arr[indexR] = arr[indexL];
        arr[indexL] = tmp;
    }
}
