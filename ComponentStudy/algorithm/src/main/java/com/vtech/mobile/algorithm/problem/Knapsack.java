package com.vtech.mobile.algorithm.problem;

public class Knapsack {

    public static void main(String[] args) {

    }

    /**
     * @param w 物品重量
     * @param v 物品价值
     * @param c 背包容量
     * @return
     */
    public static int knapsack(int[] w, int[] v, int c) {
        int n = w.length;
        int[][] dp = new int[n + 1][c + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= c; j++) {
                if (j < w[i - 1]) {
                    // 背包容量不足，不能装入第i个物品
                    dp[i][j] = dp[i - 1][j];
                } else {
                    // 能装下第i个物品
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i - 1]] + v[i - 1]);
                }
            }
        }
        // 返回背包能装下的最大价值
        return dp[n][c];
    }

    // The remaining space is greater than the content you put in.






}
