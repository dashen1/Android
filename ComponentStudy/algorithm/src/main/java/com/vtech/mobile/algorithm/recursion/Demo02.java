package com.vtech.mobile.algorithm.recursion;

public class Demo02 {

    public static void main(String[] args) {
        int[] arrInt = {2, 34, 1, 0, 0, 4, 10, 10};
        System.out.println(firstHand(arrInt,0,arrInt.length-1));
    }

    // 先手
    public static int firstHand(int[] arr, int i, int j) {
        // 只有一个数时
        if (i == j) {
            return arr[i];
        }
        // 先手的时候，我可以拿第一个，也可以拿最后一个，这两种结果进行比较，看哪个结果大
        return Math.max(
                arr[i] + lastHand(arr, i + 1, j),
                arr[j] + lastHand(arr, i, j - 1)
        );
    }

    // 后手
    public static int lastHand(int[] arr, int i, int j) {
        // 只有一个数时
        if (i == j) {
            return 0;
        }
        // 因为先手已经把最好的数拿走了，所以后面拿到的肯定是最小的数
        return Math.min(
                firstHand(arr, i + 1, j),
                firstHand(arr, i, j - 1)
        );
    }
}
