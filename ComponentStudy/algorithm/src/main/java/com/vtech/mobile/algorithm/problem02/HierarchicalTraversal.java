package com.vtech.mobile.algorithm.problem02;

import java.util.LinkedList;

public class HierarchicalTraversal {

    public static void main(String[] args) {

        TreeNode head = new TreeNode(4);
        TreeNode n1 = new TreeNode(2);
        TreeNode n2 = new TreeNode(6);
        TreeNode n3 = new TreeNode(1);
        TreeNode n4 = new TreeNode(3);
        TreeNode n5 = new TreeNode(5);
        TreeNode n6 = new TreeNode(7);
        head.left = n1;
        head.right = n2;
        n1.left = n3;
        n1.right = n4;
        n2.left = n5;
        n2.right = n6;

        levelTraversal(head);
    }

    public static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    public static void levelTraversal(TreeNode head) {
        if (head == null) {
            return;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(head);
        while (!queue.isEmpty()) {
            int curNum = queue.size();
            for (int i = 0; i < curNum; i++) {
                TreeNode curNode = queue.pop();
                System.out.println(curNode.value);
                if (curNode.left != null) {
                    queue.add(curNode.left);
                }
                if (curNode.right != null) {
                    queue.add(curNode.right);
                }
            }
        }
    }
}
