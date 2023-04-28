package com.vtech.mobile.algorithm.problem02;

public class MinimumAmountOfMoney {

    public static void main(String[] args) {

    }

    public static int solution(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        return process1(arr, 0, aim);
    }

    private static int process1(int[] arr, int i, int rest) {
        // 所有硬币都遍历完了
        if (i == arr.length) {
            return rest == 0 ? 0 : -1;
        }
        int res = -1;
        for (int k = 0; k *arr[i]< rest; k++) {
            int next = process1(arr, i + 1, rest - k * arr[i]);
            if (next != -1) { // 后续有可行结果
                res = res == -1 ? next + k : Math.min(res, next + k);
            }
        }
        return res;
    }
}
