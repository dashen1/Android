package com.vtech.mobile.algorithm.sort;

import java.util.Arrays;

public class BubbleSort {

    public static void main(String[] args) {

        int[] arrInt = {3,2,6,10,9,0,0,10};
        bubbleSort(arrInt);
        System.out.println(Arrays.toString(arrInt));
    }

    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int k = 1; k < arr.length; k++) {
                if (arr[k - 1] > arr[k]) {
                    int tmp = arr[k];
                    arr[k] = arr[k - 1];
                    arr[k - 1] = tmp;
                }
            }
        }
    }
}
