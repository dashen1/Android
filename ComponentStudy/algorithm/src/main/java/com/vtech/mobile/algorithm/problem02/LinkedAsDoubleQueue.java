package com.vtech.mobile.algorithm.problem02;

public class LinkedAsDoubleQueue {

    public static void main(String[] args) {
        System.out.println(13/10);
    }

    public static class Node<T> {
        T data;
        Node<T> next;
        Node<T> pre;

        public Node(T data) {
            this.data = data;
            next = null;
            pre = null;
        }
    }

    public static class DoubleQueueByLinked<V> {
        private Node<V> head;
        private Node<V> tail;
        private int size;

        public DoubleQueueByLinked() {
            head = null;
            tail = null;
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void pushHead(V value) {
            Node<V> cur = new Node<>(value);
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                cur.next = head;
                head.pre = cur;
                head = cur;
            }
            size++;
        }

        public void pushTail(V value) {
            Node<V> cur = new Node<>(value);
            if (tail == null) {
                head = cur;
                tail = cur;
            } else {
                tail.next = cur;
                cur.pre = tail;
                tail = cur;
            }
            size++;
        }

        public V popHead() {
            if (head == null) {
                throw new RuntimeException("链表为空！");
            }
            V ans = head.data;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.next;
                head.pre = null;
            }

            return ans;
        }

        public V popTail() {
            if (tail == null) {
                throw new RuntimeException("链表为空!");
            }
            V ans = tail.data;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                tail = tail.pre;
                tail.next = null;
            }
            return ans;
        }
    }
}
