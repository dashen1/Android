package com.vtech.mobile.algorithm.problem02;

public class MorrisTraversal {

    // 对于有左子树的结点都可以到达两次，对于没有左子树的结点只会到达一次
    // 现在问题是，对于一个能够到达两次的结点Y，是如何知道此时的cur是第一次来到Y还是第二次来到Y呢？


    public static class Node {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    public static void morris(Node root) {
        if (root == null) {
            return;
        }
        Node cur = root;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) { // 当前结点有左子树
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                // 遍历完之后 mostRight就是cur当前结点左子树上最右的结点
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue; // 继续遍历左子树，直到没有左子树
                } else { // 如果mostRight是指向cur，说明当前结点是第二次来到
                    mostRight.right = null;
                }
            }
            // 当前结点cur没有左子树，cur向右移动
            // 或者cur左子树上最右结点的右指针是指向cur，cur向右移动
            cur = cur.right;
        }
    }

    public static void morrisPre(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) { // 第一次来到这里
                    System.out.println(cur.value);
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            } else {
                // 没有左子树的情况
                System.out.println(cur.value);
            }
            cur = cur.right;
        }
    }

    public static void morrisIn(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) { // 有左子树
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            System.out.println(cur.value);
            cur = cur.right;
        }
    }

    public static void morrisPos(Node head){
        if (head==null){
            return;
        }
        Node cur = head;
        Node mostRight=null;
        while (cur!=null){
            mostRight = cur.left;
            if (mostRight!=null){
                while (mostRight.right!=null&&mostRight.right!=cur){
                    mostRight = mostRight.right;
                }
                if (mostRight.right==null){
                    mostRight.right = cur;
                    cur=cur.left;
                    continue;
                }else {
                    mostRight.right=null;
                    System.out.println(cur);
                }
            }
        }
    }
}
