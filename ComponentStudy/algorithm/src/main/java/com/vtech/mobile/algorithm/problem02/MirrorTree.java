package com.vtech.mobile.algorithm.problem02;

public class MirrorTree {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        System.out.println(isMirrorTree(root));
    }

    public static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    public static boolean compare(TreeNode root1,TreeNode root2){
        // 根结点的左右两个结点都为null,则返回true
        if (root1 == null && root2 == null) {
            return true;
        }
        // 看根结点的左右两个结点是否存在 ^ 异或，不同为1
        if (root1 == null ^ root2 == null) {
            return false;
        }

        return root1.value == root2.value && compare(root1.left, root2.right) && compare(root1.right, root2.left);
    }

    public static Boolean isMirrorTree(TreeNode root) {
        // 看根结点的左右两个结点是否存在
        if (root == null) {
            return true;
        }
        // 根结点的左右两个结点都为null,则返回true
        if (root.left == null && root.right == null) {
            return true;
        }
        if (root.left == null ^ root.right == null) {
            return false;
        }
        return compare(root.left,root.right);
    }
}
