package com.vtech.mobile.algorithm.problem;

public class ReOrderLinked {

    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;
        n5.next=null;
        Node ret = reOrderLinked(n1);
        while (ret!=null){
            System.out.println(ret.value);
            ret = ret.next;
        }
    }


    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 重排序链表
     * @param head
     * @return
     */
    public static Node reOrderLinked(Node head) {
        if (head == null || head.next == null) {
            return null;
        }
        // 1、先找到中间节点
        Node left = head;
        Node right = head;
        while (right.next != null && right.next.next != null) {
            left = left.next;
            right = right.next.next;
        }
        // 2、last 指向中间节点右边第一个节点
        right = left.next;
        // 3、将中间节点的下一个节点指向为null
        left.next = null;
        // 4、翻转中间节点后面的链表
        Node now = right;
        left = null;
        while (right != null) {
            right = now.next;
            now.next = left;
            left = now;
            now = right;
        }
        now = head;
        // 5、此时 left 为中间节点右半部分翻转后的首节点，也就是最后一个节点
        while (left != null) {
            right = left.next;
            left.next = now.next;
            now.next = left;
            now = left.next;
            left = right;
        }
        return head;
    }
}
