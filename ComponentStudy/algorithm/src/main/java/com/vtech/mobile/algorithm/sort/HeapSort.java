package com.vtech.mobile.algorithm.sort;

import java.util.Arrays;

public class HeapSort {
    public static void main(String[] args) {
        int[] arrInt = {2, 34, 1, 0, 0, 4, 10, 10};
        headSort(arrInt);
        System.out.println(Arrays.toString(arrInt));
    }

    private static void headSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 1、先调整为大根堆 从下往上
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }
        // 2、交换后调整大根堆 从上往下
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);
        while (heapSize > 0) {
            heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
        }
    }

    private static void heapify(int[] arr, int index, int heapSize) {
        // 左孩子节点
        int left = index * 2 + 1;
        while (left < heapSize) {// 有左孩子
            // 判断有没有右孩子，有右孩子的话，取左孩子和右孩子中较大的那个节点的下标
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            // 孩子节点和父节点比较
            largest = arr[largest] > arr[index] ? largest : index;
            // 父节点比孩子节点大那就不需要交换了 直接结束退出
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);
            index = largest;
            left = index * 2 + 1;
        }
    }

    private static void heapInsert(int[] arr, int index) {
        // 只要当前节点比父节点大，就交换
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public static void swap(int[] arr, int indexL, int indexR) {
        int tmp = arr[indexR];
        arr[indexR] = arr[indexL];
        arr[indexL] = tmp;
    }
}
