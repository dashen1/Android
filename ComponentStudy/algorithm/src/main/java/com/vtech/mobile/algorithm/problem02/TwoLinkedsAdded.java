package com.vtech.mobile.algorithm.problem02;

import java.util.Stack;

public class TwoLinkedsAdded {

    public static void main(String[] args) {

    }

    public static class Node {
        int data;
        Node next;

        public Node(Integer data) {
            this.data = data;
            next = null;
        }
    }

    public static class DoubleNode {
        int data;
        DoubleNode next;
        DoubleNode pre;

        public DoubleNode(int data) {
            this.data = data;
            next = null;
            pre = null;
        }
    }



    public static class ReverseList {
        public static Node reverseSingleList(Node head) {
            Node pre = null;
            Node be = null;
            while (head != null) {
                be = head.next;
                head.next = pre;
                pre = head;
                head = be;
            }
            return pre;
        }

        public static DoubleNode reverseDoubleList(DoubleNode head) {
            DoubleNode pre = null;
            DoubleNode be = null;
            while (head != null) {
                be = head.next;
                head.next = pre;
                head.pre = be;
                pre = head;
                head = be;
            }
            return pre;
        }
    }


    public Node addTwoNumbers(Node head1, Node head2) {
        int carry = 0;
        int n1 = 0;
        int n2 = 0;
        int sum = 0;
        Node head = null;
        Node tail = null;
        while (head1 != null || head2 != null) {
            n1 = head1 == null ? 0 : head1.data;
            n2 = head2 == null ? 0 : head2.data;
            sum = n1 + n2 + carry;
            if (head == null) {
                head = tail = new Node(sum % 10);
            } else {
                tail.next = new Node(sum % 10);
                tail = tail.next;
            }
            carry = sum / 10;
            if (head1 != null) {
                head1 = head1.next;
            }
            if (head2 != null) {
                head2 = head2.next;
            }
        }
        if (carry > 0) {
            tail.next = new Node(carry);
        }
        return head;
    }

    /**
     * 链表值是从大到小
     * 4 -> 3 -> 6  5 -> 4
     *
     * @param head1
     * @param head2
     * @return
     */
    public static Node addList(Node head1, Node head2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();

        while (head1 != null) {
            s1.push(head1.data);
            head1 = head1.next;
        }

        while (head2 != null) {
            s2.push(head2.data);
            head2 = head2.next;
        }

        int carry = 0; // 进位
        int n1 = 0;
        int n2 = 0;
        int n = 0;
        Node node = null;
        Node pre = null;
        while (!s1.isEmpty() || !s2.isEmpty()) {
            n1 = s1.isEmpty() ? 0 : s1.pop();
            n2 = s2.isEmpty() ? 0 : s2.pop();
            n = n1 + n2 + carry;
            pre = node;
            node = new Node(n % 10);
            node.next = pre;
            carry = n / 10;
        }
        if (carry == 1) {
            pre = node;
            node = new Node(1);
            node.next = pre;
        }
        return node;
    }
}
