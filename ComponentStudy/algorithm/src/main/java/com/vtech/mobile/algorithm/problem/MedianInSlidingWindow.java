package com.vtech.mobile.algorithm.problem;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.PriorityQueue;

public class MedianInSlidingWindow {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {

        PriorityQueue<Integer> left = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        left.add(2);
        left.add(1);
        System.out.println(left.peek());

        int[] arr = {1, 3, -1, -3, 5, 3, 6, 7};
        double[] ret1 = medianSlidingWindow(arr, 3);
        //double[] ret2 = medianSlidingWindow2(arr, 3);
        System.out.println("First method : " + Arrays.toString(ret1));
        //System.out.println("Second method : " + Arrays.toString(ret2));
    }

    public static double[] medianSlidingWindow(int[] nums, int k) {
        int len = nums.length;
        int countOdWindow = len - k + 1;
        double[] ans = new double[countOdWindow];
        int[] tmp = new int[k];
        // l:0 开始
        // r:2 开始
        for (int l = 0, r = l + k - 1; r < len; l++, r++) {
            for (int i = l; i <= r; i++) {
                tmp[i - l] = nums[i];
            }
            Arrays.sort(tmp);
            ans[l] = (tmp[k / 2] / 2.0) + (tmp[(k - 1) / 2] / 2.0);
        }

        return ans;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double[] medianSlidingWindow2(int[] nums, int k) {
        int len = nums.length;
        int countOfWindow = len - k + 1;
        double[] ans = new double[countOfWindow];
        // 如果是奇数滑动窗口，让right的数量比left多一个
        PriorityQueue<Integer> left = new PriorityQueue<>((a, b) -> Integer.compare(b, a)); // 大根堆
        PriorityQueue<Integer> right = new PriorityQueue<>((a, b) -> Integer.compare(a, b)); // 小根堆
        for (int i = 0; i < k; i++) {
            right.add(nums[i]);
        }
        for (int i = 0; i < k / 2; i++) {
            left.add(right.poll());
        }
        ans[0] = getMedian(left, right);
        for (int i = k; i < len; i++) {
            // 人为确保right会比left多，因此，删除和添加都与right比较，（left可能为空）
            int add = nums[i], del = nums[i - k];
            // 如果要添加的节点在最大一半的右边，就添加到右边，否则添加到左边
            if (add >= right.peek()) {
                right.add(add);
            } else {
                left.add(add);
            }
            // 要删除的节点在最大一半的右边，就从右边删除，否则删除左边的
            if (del >= right.peek()) {
                right.remove(del);
            } else {
                left.remove(del);
            }
            adjustHeap(left, right);
            ans[i - k + 1] = getMedian(left, right);
        }
        return ans;
    }

    private static double getMedian(PriorityQueue<Integer> left, PriorityQueue<Integer> right) {
        if (left.size() == right.size()) {
            return (left.peek() / 2.0) + (right.peek() / 2.0);
        } else {
            return right.peek() * 1.0;
        }
    }

    private static void adjustHeap(PriorityQueue<Integer> left, PriorityQueue<Integer> right) {
        // 人为保持右边永远比左边的多一个节点
        while (left.size() > right.size()) {
            right.add(left.poll());
        }
        while (right.size() - left.size() > 1) {
            left.add(right.poll());
        }
    }

}
