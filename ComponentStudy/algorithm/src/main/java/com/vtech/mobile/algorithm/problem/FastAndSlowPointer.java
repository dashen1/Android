package com.vtech.mobile.algorithm.problem;

import java.util.Stack;

public class FastAndSlowPointer {

    public static void main(String[] args) {

        Node head = new Node(1);
        Node n1 = new Node(3);
        Node n2 = new Node(3);
        Node n3 = new Node(2);
        Node n4 = new Node(1);
        head.next = n1;
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = null;
        System.out.println(isPalindrome3(head));
    }

    public static class Node {

        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 解法1：将链表全部节点进栈出栈比较(空间复杂度O(n))
     * 使用栈和快慢指针判断一个链表里面的元素是否是回文
     *
     * @return
     */
    public static boolean isPalindrome1(Node head) {
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }
        while (head != null) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    // 解法2：将链表右半部分节点进栈出栈比较(空间复杂度O(n/2))
    public static boolean isPalindrome2(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        Node right = head.next; // right指针最后指向中点右边第一个节点
        Node cur = head;
        while (cur.next != null && cur.next.next != null) {
            right = right.next;
            cur = cur.next.next;
        }

        Stack<Node> stack = new Stack<>();
        while (right != null) { // 将右半部分放进栈中
            stack.push(right);
            right = right.next;
        }
        while (!stack.isEmpty()) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    // 解法3：快慢指针+指针逆序解法（空间复杂度O(1)）

    /**
     * 1、先用快慢指针找到中点位置，我们想让快指针走两步，慢指针走一步，这样当快指针结束的时候，慢指针刚好来到中点的位置
     * 2、将慢指针指向的节点指向null,从中点处往下遍历的时候将后面的指针逆序
     * 3、链表头尾各用一个引用标记，然后同时往中中间遍历，每次走一步，直到一边为null,则停止，期间两边引用指向的值相同，则说明是回文，
     * 最后返回结果前将逆序指针回复原状
     */
    public static boolean isPalindrome3(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        fast = slow.next;// fast指向中间节点的右边第一个节点
        slow.next = null; // 将中间节点的下一个节点指向为null
        Node n3 = null;
        while (fast != null) {
            n3 = fast.next;
            fast.next = slow;
            slow = fast;
            fast = n3;
        }
        n3 = slow; // 保存最后一个节点
        fast = head; // 左边第一个节点
        boolean res = true;
        while (slow != null && fast != null) {
            if (slow.value != fast.value) {
                return false;
            }
            slow = slow.next; // 从左边往中间走
            fast = fast.next; // 从右边往中间走
        }
        slow = n3.next;
        n3.next = null;
        while (slow != null) { // 将逆序复原
            fast = slow.next;
            slow.next = n3;
            n3 = slow;
            slow = fast;
        }
        return true;
    }
}
