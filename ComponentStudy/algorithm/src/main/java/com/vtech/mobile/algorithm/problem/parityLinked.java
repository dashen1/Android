package com.vtech.mobile.algorithm.problem;

public class parityLinked {

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
        Node ret = parityLinkedWithO_1(n1);
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

    /** 奇偶链表
     * 示例 1:
     *
     * 输入: 1->2->3->4->5->NULL
     *
     * 输出: 1->3->5->2->4->NULL
     * @param head
     * @return
     */
    public static Node parityLinkedWithO_1(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node odd_tail = head;
        Node even_head = head.next;
        Node even_tail = head.next;
        while (odd_tail.next != null && even_tail.next != null) {
            odd_tail.next = even_tail.next;
            odd_tail = odd_tail.next;
            even_tail.next = odd_tail.next;
            even_tail = even_tail.next;
        }
        odd_tail.next = even_head;
        return head;
    }
}
