package com.vtech.mobile.algorithm.problem;

public class BinaryTreeHeight {

    public static void main(String[] args) {

        int[] arr = {1, 2, 4, 3, 6};
        System.out.println("The biggest value : " + maxValue2(arr));
    }

    public static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static class Solution {
        public int TreeDepth(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int left = TreeDepth(root.left);
            int right = TreeDepth(root.right);
            return Math.max(left, right) + 1;
        }
    }

    public static class BalanceBinaryTree {
        public boolean isBalancedTree(TreeNode root) {
            if (root == null) {
                return true;
            }
            int left = maxDepth(root.left);
            int right = maxDepth(root.right);
            if (Math.abs(left - right) > 1) {
                return false;
            }
            return true;
        }

        public int maxDepth(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int left = maxDepth(root.left);
            int right = maxDepth(root.right);
            return Math.max(left, right) + 1;
        }
    }

    public static int addSum(int m) {
        if (m < 2) {
            return 1;
        } else {
            return m + addSum(m - 1);
        }
    }

    public static int fo(int n) {
        if (n < 2) {
            return 1;
        } else {
            return n * fo(n - 1);
        }
    }

    // The max value in array
    public static int maxValue(int[] arr) {
        if (arr == null || arr.length < 1) {
            return 0;
        }
        return 0;
    }

    public static int maxValue2(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    static int res = Integer.MIN_VALUE;

    /**
     * @param arr
     * @param num 数组最大下标
     * @return
     */
    public static int getMax2(int[] arr, int num) {
        if (num == 0) {
            return res;
        }
        res = Math.max(arr[num], getMax2(arr, num - 1));
        return res;
    }

    public static int getMax(int[] arr) {
        return process(arr, 0, arr.length - 1);
    }

    private static int process(int[] arr, int left, int right) {
        int mid = left + ((right - left) >> 1);
        if (left == right) {
            return arr[left];
        }
        int left_max = process(arr, left, mid);
        int right_max = process(arr, mid + 1, right);
        return Math.max(left_max, right_max);
    }
}
