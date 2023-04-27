package com.vtech.mobile.algorithm.problem02;

import java.util.Stack;

public class KNodeReverse {

    public static class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
            next = null;
        }
    }

    // 不需要借助栈，直接在链表内翻转
    public static Node reverseKNode2(Node head, int k) {
        if (k < 2) {
            return head;
        }
        Node cur = head;
        Node start = null;
        Node pre = null;
        Node next = null;
        int count = 0;
        while (cur != null) {
            next = cur.next;
            if (count == k) {
                start = pre == null ? head : pre.next;
                head = pre == null ? cur : head;
                resign2(pre, start, cur, next);
                pre = start;
                count = 0;
            }
            count++;
            cur = next;
        }
        return head;
    }

    private static void resign2(Node left, Node start, Node end, Node right) {
        Node pre = start;
        Node cur = start.next;
        Node next = null;
        while (cur != right) {
            next = cur.next;
            cur.next = pre; // 结点逆序
            pre = cur;
            cur = next;
        }
        if (left != null) {
            left.next = end;
        }
        start.next = right;
    }

    // 借助栈进行翻转
    public static Node reverseKNodes1(Node head, int k) {
        if (k < 2) {
            return head;
        }
        Stack<Node> stack = new Stack<>();
        Node newHead = head;
        Node cur = head;
        Node pre = null;
        Node next = null;
        while (cur != null) {
            next = cur.next;
            stack.push(cur);
            if (stack.size() == k) {
                pre = resign1(stack, pre, next);
                newHead = newHead == head ? cur : newHead;// 这个主要用于第一组翻转后新的头节点
            }
            cur = next;
        }
        return newHead;
    }

    /**
     * 将栈中的结点弹出并连接在left和right中间
     *
     * @param stack
     * @param left
     * @param right
     * @return
     */
    private static Node resign1(Stack<Node> stack, Node left, Node right) {
        Node cur = stack.pop();
        if (left != null) {
            left.next = cur;
        }
        Node next = null;
        while (!stack.isEmpty()) {
            next = stack.pop();
            cur.next = next;
            cur = next;
        }
        cur.next = right;
        return cur;
    }


}
