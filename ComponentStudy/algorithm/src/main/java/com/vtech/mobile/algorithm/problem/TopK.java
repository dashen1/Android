package com.vtech.mobile.algorithm.problem;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TopK {

    public static void main(String[] args) {
        int[] arr = {12,2,23,34,3,1};
        int[] tmp = topK1(arr,3);
        System.out.println(Arrays.toString(tmp));
    }


    // 只含k个最小的元素
    public static int[] topK1(int[] arr, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // 逆序大根堆
                return o2 - o1;
            }
        });
        // 放入前k个元素
        for (int i = 0; i < arr.length; i++) {
            if (i < k) {
                maxHeap.add(arr[i]);
            } else {
                // 从第k个元素开始进行判断是否要入堆
                if (maxHeap.peek() > arr[i]) {
                    maxHeap.poll();
                    // add()方法实际也是调用offer()方法
                    maxHeap.offer(arr[i]);
                }
            }
        }
        int[] ret = new int[k];
        for (int i = 0; i < k; i++) {
            ret[i] = maxHeap.poll();
        }
        return ret;
    }

}
