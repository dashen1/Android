package com.vtech.mobile.algorithm.sort;

import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {

        int[] arrInt = {2, 34, 1, 0, 0, 4, 10, 10};
        quickSort(arrInt);
        System.out.println(Arrays.toString(arrInt));
    }

    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    private static void process(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        int[] equal = partition(arr, L, R);
        process(arr, L, equal[0] - 1);
        process(arr, equal[1] + 1, R);
    }

    private static int[] partition(int[] arr, int L, int R) {
        int lessR = L - 1;
        int moreL = R;
        int index = L;
        while (index < moreL) {
            if (arr[index] < arr[R]) {
                swap(arr, ++lessR, index++);
            } else if (arr[index] > arr[R]) {
                swap(arr, --moreL, index);
            } else {
                index++;
            }
        }
        swap(arr, moreL, R);
        return new int[]{lessR + 1, moreL};
    }

    public static void swap(int[] arr, int indexL, int indexR) {
        int tmp = arr[indexR];
        arr[indexR] = arr[indexL];
        arr[indexL] = tmp;
    }
}
