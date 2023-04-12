package com.vtech.mobile.algorithm.problem;

import java.util.Arrays;
import java.util.LinkedList;

public class SlidingWindow {

    public static void main(String[] args) {
        int[] arr = {4,3,5,4,3,3,6,7};
        int[] maxWindow = getMaxWindow(arr,3);
        System.out.println(Arrays.toString(maxWindow));
    }

    // 利用双端队列

    /** 窗口最大值更新结构
     * @param arr
     * @param w   窗口大小
     * @return
     *
     * res[i] 表示每一种窗口状态下的最大值
     */
    public static int[] getMaxWindow(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        LinkedList<Integer> qmax = new LinkedList<>();
        int[] res = new int[arr.length - w + 1];
        int index = 0;
        // 先看队列有没有小于当前要放入的值，有就弹出再放入
        for (int i = 0; i < arr.length; i++) {
            while (!qmax.isEmpty() && arr[qmax.peekLast()] < arr[i]) {
                qmax.pollLast();
            }
            qmax.addLast(i);
            if (i - qmax.peekFirst() == w) { // 窗口大小已经大于规定的最大窗口了，需要弹出
                qmax.pollFirst();
            }
            if (i >= w - 1) { // 窗口形成 从第三个元素开始，每向右移动一位都会形成新的窗口
                res[index++] = arr[qmax.peekFirst()];
            }
        }
        return res;
    }
}
