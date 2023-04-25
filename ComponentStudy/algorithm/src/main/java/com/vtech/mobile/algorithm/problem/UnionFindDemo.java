package com.vtech.mobile.algorithm.problem;

import java.util.Arrays;
import java.util.HashMap;

public class UnionFindDemo {

    public static void main(String[] args) {
        UnionFindDemo unionFindDemo = new UnionFindDemo();
        // [1,2], [1,3], [2,3]
        int[][] arrs1 = new int[][]{{1,2},{1,3},{2,3}};
        // [1,2], [2,3], [3,4], [1,4], [1,5]
        int[][] arrs2 = new int[][]{{1,2},{2,3},{3,4},{1,4},{1,5}};
        int[] ret = unionFindDemo.findRedundantConnection(arrs2);
        System.out.println(Arrays.toString(ret));
    }


    public int[] findRedundantConnection(int[][] edges) {
        UnionFind uf = new UnionFind();
        for (int i = 0; i < edges.length; i++) {
            if (uf.isConnected(edges[i][0], edges[i][1])) {
                return edges[i];
            } else {
                uf.add(edges[i][0]);
                uf.add(edges[i][1]);
                System.out.println("begin");
                uf.merge(edges[i][0], edges[i][1]);
                System.out.println("end");
            }
        }
        return edges[0];
    }

    class UnionFind{
        HashMap<Integer, Integer> father;
        int numset;

        public UnionFind() {
            numset = 0;
            father = new HashMap<>();
        }

        public void add(int x) {
            if (!father.containsKey(x)) {
                father.put(x, null);
                numset++;
            }
        }

        public int find(int x) {
            int root = x;
            while (father.get(root) != null) {
                root = father.get(root);
            }
            System.out.println("start : "+root);
            // 压缩 将整个树结构都压缩为深度为2的树 也就是说所有孩子结点的父结点都变成根结点
            while (father.get(x) != null) {
                int ori_father = father.get(x);
                System.out.println("find : "+x+" "+root);
                father.put(x, root);
                x = ori_father;
            }
            return root; // 返回所有结点的根结点
        }

        public void merge(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            // 说明是不同的两颗树
            if (rootX != rootY) {
                father.put(rootX, rootY);
                numset--;
            }
        }

        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }

        public int getNumset() {
            return numset;
        }

    }
}
