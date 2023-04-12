package com.vtech.mobile.algorithm.problem;

import java.util.Arrays;

public class RevertNumber {

    public static void main(String[] args) {
        int[] res = new int[4];
        int num = revert(res, 0, 1234);
        System.out.println(Arrays.toString(res));
        System.out.println(num);
    }

    public static int revert(int[] res, int i, int number) {
        if (i < res.length) {
            res[i] = number % 10; // 求余得到个位数
            number = (number - number % 10) / 10;
            return revert(res, i + 1, number);
        } else {
            return 0;
        }
    }

}
