package com.vtech.mobile.algorithm.problem02;

public class NQueue {

    public static void main(String[] args) {

    }

    public static int nQueue(int n){
        if (n<0){
            return 0;
        }
        // record[0] 表示第0行的皇后放在哪一列
        // 下标和值分别对应行和列
        int[] record = new int[n];
        return process1(0,record,n);
    }

    /**
     *
     * @param i 当前来到了第几行
     * @param record
     * @param n
     * @return
     */
    private static int process1(int i, int[] record, int n) {
        // 所有行都遍历完了
        if (i==n){
            return 1;
        }
        int res = 0;
        // 当前行在i行，需要尝试所有列
        for (int j = 0; j < n; j++) {
            // 当前i行的皇后，放在j列，不能和之前(0,i-1)的皇后，共行或者共列
            if (isValid(record,i,j)){
                record[i] = j; // 如果合法，就放在此列
                res+=process1(i+1,record,n); // 继续往下一行遍历
            }
        }
        return res;
    }

    private static boolean isValid(int[] record, int i, int j) {
        // 当前i行前面的所有行的皇后
        for (int k = 0; k < i; k++) {
            // 共列共斜线返回false
            if (j==record[k]||Math.abs(record[k]-j)==Math.abs(i-k)){
                return false;
            }
        }
        return true;
    }
}
