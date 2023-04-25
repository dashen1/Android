package com.vtech.mobile.algorithm.problem;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class MinimumEffortPath {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {

        // 2
        MinimumEffortPath m = new MinimumEffortPath();
        int min = m.minimumEffortPath2(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}});
        System.out.println(min);
    }

    int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * 第一种方式
     * 二分+bfs(广度优先遍历)
     */
    public int minimumEffortPath1(int[][] heights) {
        int m = heights.length;
        int n = heights[0].length;
        // 因为 1<=heights[i][j]<=1000000,所以体力消耗值在 0-99999中
        // 所以在这个区间查找就可以了
        int left = 0, right = 99999, ans = 0;
        while (left <= right) { // 当left==right时，left和right和mid都是最小体力消耗值
            int mid = (left + right) / 2;
            LinkedList<int[]> queue = new LinkedList<>();
            // 加入初始点
            queue.offer(new int[]{0, 0});
            // 记录当前顶点是否被访问
            boolean[] seen = new boolean[m * n];
            seen[0] = true;
            // bfs遍历整张图，把体力消耗值<=mid的全部走一遍、
            while (!queue.isEmpty()) {
                int[] cell = queue.poll();
                int x = cell[0], y = cell[1];
                for (int i = 0; i < 4; i++) {
                    // 将当前顶点的上下左右顶点遍历
                    int nx = x + dirs[i][0];
                    int ny = x + dirs[i][1];
                    // 如果上下左右顶点没有越界，并且体力消耗值<=mid
                    // 我们就走这个点，并标志为已访问过
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && !seen[nx * n + ny] && Math.abs(heights[x][y] - heights[nx][ny]) <= mid) {
                        queue.offer(new int[]{nx, ny});
                        seen[nx * n + ny] = true;
                    }
                }
            }
            // 遍历完图之后判断最后一个顶点访问过没有
            if (seen[m * n - 1]) {
                // 访问过了，说明最小体力消耗值在当前区间mid中点的左区间
                ans = mid;
                right = mid - 1;
            } else {
                // 没有访问过，说明最小体力消耗值在当前区间mid中点的右区间
                left = mid + 1;
            }
        }
        return ans;
    }


    int[] parent;
    int[] rank;

    /**
     * 并查集+Kruskal(最小生成树)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int minimumEffortPath2(int[][] heights) {
        int row = heights.length;
        int col = heights[0].length;
        parent = new int[row * col];// 父节点数组
        rank = new int[row * col];
        // 数组存储两个结点和两结点的权值，并用小根堆存储
        PriorityQueue<int[]> pq = new PriorityQueue<>((arr1, arr2) -> arr1[2] - arr2[2]);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                // 二位数组一维化
                int index = i * col + j;
                parent[index] = index;// 父结点数组初始化
                // 将当前结点的上和左结点移动到当前结点的体力值记录的数组中，并存储到小根堆
                if (i > 0) {
                    pq.offer(new int[]{index - col, index, Math.abs(heights[i][j] - heights[i - 1][j])});
                }
                if (j > 0) {
                    pq.offer(new int[]{index - 1, index, Math.abs(heights[i][j] - heights[i][j - 1])});
                }
            }
        }
        int min = 0;// 最小体力值
        while (!pq.isEmpty()) {
            // 依次取出小根堆中的值，也就是一直取最小体力值
            int[] nums = pq.poll();
            // 合并节点
            union(nums[0], nums[1]);
            if (find(0) == find(row * col - 1)) {
                // 如果开始结点和最后的结点相通，说明已经连通了，当前体力值就是最小花费体力值
                min = nums[2];
                break;
            }
        }
        return min;
    }

    private int find(int index) {
        while (index != parent[index]) {
            parent[index] = parent[parent[index]];
            index = parent[index];
        }
        return index;
    }

    private void union(int index1, int index2) {
        int root1 = find(index1);
        int root2 = find(index2);
        if (root1 == root2) return;
        // rank[root] 表示以root为根结点的孩子结点个数，孩子结点个数少的挂到孩子结点个数多的root结点上去
        if (rank[root1] > rank[root2]) {
            parent[root2] = root1;
        } else if (rank[root1] < rank[root2]) {
            parent[root1] = root2;
        } else {
            parent[root2] = root1;
            rank[root1]++;
        }
    }


    // int[][] dirs = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    /**
     * Dijkstra(单元最短路径)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int minimumEffortPath3(int[][] heights) {
        int row = heights.length;
        int col = heights[0].length;
        int length = row * col;
        // 存储从(0,0)顶点到当前结点的所需要花费的最小体力值数组
        int[] dists = new int[length];
        // 设置数组初始值为int的最大值
        Arrays.fill(dists, Integer.MAX_VALUE);
        // 设置起点最小体力值为0
        dists[0] = 0;
        // 记录顶点是否访问过数组
        boolean[] visited = new boolean[length];
        // 存储边的优先队列 小根堆
        PriorityQueue<int[]> pq = new PriorityQueue<>((arr1, arr2) -> arr1[2] - arr2[2]);
        pq.offer(new int[]{0, 0, 0});// 添加起始数组
        while (!pq.isEmpty()) {
            int[] poll = pq.poll();
            int x = poll[0], y = poll[1], d = poll[2];
            // 当前顶点的一维坐标
            int index = x * col + y;
            if (visited[index]) {
                // 如果当前结点已经访问过，直接取下一个结点
                continue;
            }
            visited[index] = true; // 标记当前结点已被访问过
            if (index == length - 1) { // 如果到达了最后顶点，直接跳出循环
                break;
            }
            for (int i = 0; i < dirs.length; i++) {
                int nx = x + dirs[i][0];
                int ny = y + dirs[i][1];
                if (nx >= 0 && ny >= 0 && nx < row && ny < col && Math.max(d, Math.abs(heights[nx][ny] - heights[x][y])) < dists[nx * col + ny]) {
                    // 当向上右下左走一个顶点，并且走完这个顶点花费的体力值要比其它路径花费的小
                    // 更新结点的体力值，并将结点加入队列
                    dists[nx * col + ny] = Math.max(d, Math.abs(heights[nx][ny] - heights[x][y]));
                    pq.offer(new int[]{nx, ny, dists[nx * col + ny]});
                }
            }
        }
        return dists[length - 1];
    }


}
