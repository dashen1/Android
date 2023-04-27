package com.vtech.mobile.algorithm.problem02;

public class PrefixTree {

    public static void main(String[] args) {

    }

    public static class TreeNode {
        int path; // 有多少字符串达到过这个结点
        int end; // 有多少字符串以这个结点结尾
        TreeNode[] nexts;

        public TreeNode() {
            path = 0;
            end = 0;
            nexts = new TreeNode[26];
        }
    }

    public static class Tree {
        private TreeNode root;

        public Tree() {
            root = new TreeNode();
        }

        // 插入字符串
        public void insert(String str) {
            if (str == null) {
                return;
            }
            char[] chars = str.toCharArray();
            TreeNode node = root;
            int index = 0;
            for (int i = 0; i < chars.length; i++) {
                index = chars[i] - 'a'; // ASCII 码
                if (node.nexts[index] == null) {
                    node.nexts[index] = new TreeNode();
                }
                node = node.nexts[index];
                node.path++; // 沿途path都++
            }
            node.end++; // 最后一个结点++
        }

        public void delete(String str) {
            if (search(str) != 0) {
                char[] chars = str.toCharArray();
                TreeNode node = root;
                int index = 0;
                for (int i = 0; i < chars.length; i++) {
                    index = chars[i] - 'a';
                    // 根据path值，来决定是否释放掉那个结点
                    // 因为 path 可能 >= end
                    if (--node.nexts[index].path == 0) {
                        node.nexts[index] = null;
                        return;
                    }
                    node = node.nexts[index];
                }
                node.end--;
            }
        }

        // 查找字符串插入了几次
        public int search(String str) {
            if (str == null) {
                return 0;
            }
            char[] chars = str.toCharArray();
            TreeNode node = root;
            int index = 0;
            for (int i = 0; i < chars.length; i++) {
                index = chars[i] - 'a';
                if (node.nexts[index] == null) {
                    return 0;
                }
                node = node.nexts[index];
            }
            return node.end;
        }

        public int prefixNumber(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chars = pre.toCharArray();
            TreeNode node = root;
            int index = 0;
            for (int i = 0; i < chars.length; i++) {
                index = chars[i] - 'a';
                if (node.nexts[index] == null) {
                    return 0;
                }
                node = node.nexts[index];
            }
            return node.path;
        }
    }
}
