package com.vtech.mobile.algorithm.problem02;

import java.util.PriorityQueue;

public class GoldBar {

    public static void main(String[] args) {

    }

    public static int lessMoney(int[] arr){
        // 优先级队列，默认升序，也就是小根堆
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i = 0; i < arr.length; i++) {
            queue.add(arr[i]);
        }
        int sum=0;
        int cur = 0;
        while (queue.size()>1){
            cur=queue.poll()+queue.poll();
            sum+=cur;
            queue.add(cur);
        }
        return sum;
    }
}
