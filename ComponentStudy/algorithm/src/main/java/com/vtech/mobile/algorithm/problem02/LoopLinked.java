package com.vtech.mobile.algorithm.problem02;

public class LoopLinked {


    public static class TreeNode {
        int value;
        TreeNode next;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    // 快慢指针 判断一个链表是否有环
    // 如果一个链表无环，那么最后一个结点一定会指向null,否则指向其它结点
    // 快指针（走两）慢指针（走一步） 如果有环一定会在环上相遇
    // 相遇后快指针从头开始，一次一步，慢指针在原地继续一次一步，再次相遇时就是在入环结点处
    public static TreeNode getLoopNode(TreeNode head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        TreeNode n1 = head.next;
        TreeNode n2 = head.next.next;
        while (n1 != n2) {
            if (n2.next == null || n2.next.next == null) {
                return null;
            }
            n2 = n2.next.next;
            n1 = n1.next;
        }
        n2 = head;
        while (n1 != n2) {
            n1 = n1.next;
            n2 = n2.next;
        }
        return n1;
    }

}
