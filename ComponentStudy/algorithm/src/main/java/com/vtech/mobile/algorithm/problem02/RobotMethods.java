package com.vtech.mobile.algorithm.problem02;

public class RobotMethods {

    public static void main(String[] args) {

    }

    /**
     * 机器人只能向左或向右移动
     * 返回从i位置到end位置的总方法数
     *
     * @param N    总共有N个位置
     * @param i    现在在第i个位置
     * @param rest 剩下rest步
     * @param end  需要到达end的位置
     * @return
     */
    public static int process1(int N, int i, int rest, int end) {
        if (rest == 0) {
            return i == end ? 1 : 0;
        }
        if (i == 1) { // 开始位置是从1开始，所以如果当前所在位置为1，就只能往2的位置上走
            return process1(N, 2, rest - 1, end);
        }
        if (i == N) { // 同理，如果当前位置已经是最终要到达的位置，就只能往前走
            return process1(N, N - 1, rest - 1, end);
        }
        // 如果当前所在位置在中间部分，就既可以往前走，也可以往回走
        return process1(N, i + 1, rest - 1, end) + process1(N, i - 1, rest - 1, end);
    }

    public static int process2(int N, int i, int rest, int end, int[][] dp) {
        if (dp[rest][i] != -1) {
            return dp[rest][i];
        }
        if (rest == 0) {
            dp[rest][i] = i == end ? 1 : 0;
        }
        if (i == 1) {
            dp[rest][i] = process2(N, 2, rest - 1, end, dp);
        }
        if (i == N) {
            dp[rest][i] = process2(N, N - 1, rest - 1, end, dp);
        }
        dp[rest][i] = process2(N, i - 1, rest - 1, end, dp) + process2(N, i + 1, rest - 1, end, dp);
        return dp[rest][i];
    }

    public static int robot2(int N, int i, int rest, int end) {
        int[][] dp = new int[rest + 1][N + 1];
        // 剩余步数为0，当前位置为end时
        // dp[end][0]=1;
        for (int row = 0; row < dp.length; row++) {
            for (int col = 0; col < dp[0].length; col++) {
                dp[row][col] = -1;
            }
        }
        return process2(N, i, rest, end, dp);
    }

    public static int process3(int N, int i, int rest, int end, int[][] dp) {
        for (int j = 0; j < dp[0].length; j++) {
            dp[0][j] = i == end ? 1 : 0;
        }
        for (int step = 1; step < dp.length; step++) {
            for (int cur = 1; cur < dp[0].length; cur++) {
                if (cur == 1) { // 当前位置在开始位置，只能往右走
                    dp[step][cur] = dp[step - 1][cur + 1];
                } else if (cur == N) { // 当前位置已经在终点位置,只能往回走
                    dp[step][cur] = dp[step - 1][cur - 1];
                } else { // 当前位置在中间某个位置，既可以往左走，也可以往右走
                    dp[step][cur] = dp[step - 1][cur + 1] + dp[step - 1][cur - 1];
                }
            }
        }
        return dp[rest][i];
    }

    public static int robot3(int N, int i, int rest, int end) {
        int[][] dp = new int[rest + 1][N + 1];
        return process3(N, i, rest, end, dp);
    }

}
