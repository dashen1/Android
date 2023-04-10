package com.vtech.mobile.algorithm.sort;

import java.util.Arrays;

public class SelectionSort {

    public static void main(String[] args) {
        int[] arrInt = {2, 34, 1, 0, 0, 4, 10, 10};
        selectionSort(arrInt);
        System.out.println(Arrays.toString(arrInt));
    }

    private static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        int N = arr.length;
        // 1、可以选着的个数，最后一个不需要选择了
        for (int i = 0; i < N - 1; i++) {
            int minValueIndex = i;
            for (int j = i + 1; j < N; j++) {
                if (arr[minValueIndex] > arr[j]) {
                    minValueIndex = j;
                }
            }
            if (arr[i] != arr[minValueIndex]) {
                swap(arr, i, minValueIndex);
            }
        }
    }


    public static void swap(int[] arr, int indexL, int indexR) {
        int tmp = arr[indexR];
        arr[indexR] = arr[indexL];
        arr[indexL] = tmp;
    }
}
