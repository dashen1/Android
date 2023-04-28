package com.vtech.mobile.algorithm.problem02;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MaxMoneyIPO {

    public static void main(String[] args) {

    }

    public static class Node {
        int profit;
        int cost;

        public Node(int profit, int cost) {
            this.profit = profit;
            this.cost = cost;
        }
    }

    // 最小花费比较器 小根堆
    public static class MiniCostComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.cost - o2.cost;
        }
    }

    // 最大利润比较器 大根堆
    public static class MaxProfitComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o2.profit - o1.profit;
        }
    }

    /**
     * @param k       只能串行的最多做K个项目
     * @param w       表示初始资金
     * @param profits profits[i] 表示i号项目在扣除花费之后还能挣到的钱（利润）
     * @param capital capital[i] 表示i号项目的花费
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        PriorityQueue<Node> minCostQ = new PriorityQueue<>(new MiniCostComparator());
        PriorityQueue<Node> maxProfitQ = new PriorityQueue<>(new MaxProfitComparator());
        // 将所有项目队列--由花费组成的小根堆
        // init nodes
        Node[] nodes = new Node[profits.length];
        for (int i = 0; i < profits.length; i++) {
            nodes[i] = new Node(profits[i], capital[i]);
        }
        for (int i = 0; i < nodes.length; i++) {
            minCostQ.add(nodes[i]);
        }
        // 进行k轮看
        for (int i = 0; i < k; i++) {
            // 能力所以的项目，全部解锁
            while (!minCostQ.isEmpty() && minCostQ.peek().cost <= w) {
                maxProfitQ.add(minCostQ.poll());
            }
            // 所有项目都不能解锁 初始资金不够
            if (maxProfitQ.isEmpty()) {
                return w;
            }
            w += maxProfitQ.poll().profit;
        }
        return w;
    }
}
