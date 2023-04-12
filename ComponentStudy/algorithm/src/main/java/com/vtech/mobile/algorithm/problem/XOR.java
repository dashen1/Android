package com.vtech.mobile.algorithm.problem;

public class XOR {

    public static void main(String[] args) {
        int[] arrInt = {2,2,2,1,1,4,4,4,4,4,5,5};
        findOddTimesNum(arrInt);
    }

    /**
     * 一个数组中有梁中书出现奇次，其他数出现了偶次，找到并打印这两种奇次数
     */

    public static void findOddTimesNum(int[] arr) {
        int eor = 0;
        //{2,2,2,1,1,4,4,4,4,4,5,5}
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        // eor = 2^1
        // 找到最右边的1，也就是这两个奇数右边不同的位
        // 取反+1 & 自己
        int rightOne = eor & (~eor + 1);
        int firstOdd = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((rightOne & arr[i]) != 0) {
                firstOdd ^= arr[i];
            }
        }
        // 因为 eor = a^b, firstOdd 可能是 a或b ，假设firstOdd = a,则 eor^firstOdd = b 就得到答案了
        System.out.println("firstOdd : " + firstOdd + " secondOdd : " + (eor ^ firstOdd));
    }
}
