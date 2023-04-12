package com.vtech.mobile.algorithm.problem;

public class Manacher {

    public static void main(String[] args) {

    }

    public static char[] manacherStr(String str) {
        char[] charArr = str.toCharArray();
        char[] res = new char[charArr.length * 2 + 1];
        int index = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : charArr[index++];
        }
        return res;
    }

    // 最长回文直径
    public static int maxLength(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        char[] charArr = manacherStr(s);
        int[] pArr = new int[charArr.length]; // 回文半径数组
        int C = -1; // 回文中心
        int R = -1; // 回文右边界再往右一个位置 最右的有效区域 R-1
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < charArr.length; i++) {
            // i至少的回文区域， 先给pArr[i]
            // [ ... i`...C...i...R...] i`位置等于2*C-i = C-(i-C)
            // 统一求一个不用验证的区域 至少不用验证的区域
            pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1;
            // 统一都往外扩 省去if-else
            while (i + pArr[i] < charArr.length && i - pArr[i] > -1) {
                if (charArr[i + pArr[i]] == charArr[i - pArr[i]]) {
                    pArr[i]++;
                } else {
                    break;
                }
            }
            if (i + pArr[i] > R) {
                R = i + pArr[i];
                C = i;
            }
            max = Math.max(max, pArr[i]);
        }
        return max - 1;
    }
}
