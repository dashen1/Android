package com.vtech.mobile.algorithm.problem02;

public class LinkedAsQueue {

    public static void main(String[] args) {

    }

    public static class Node<T> {
        T data;
        Node next;

        public Node(T data) {
            this.data = data;
            next = null;
        }
    }

    /**
     * 单链表做队列
     * 需要两个指针
     * head:头指针用来删除结点，出队
     * tail:尾结点用于添加结点，防止每次都从头遍历
     *
     * @param <V>
     */
    public static class QueueByLinked<V> {
        private Node<V> head;
        private Node<V> tail;
        private int size;

        public QueueByLinked() {
            head = null;
            tail = null;
            size = 0;
        }

        public boolean isEmpty() {
            return head == null;
        }

        public int getSize() {
            return size;
        }

        public void enqueue(V value) {
            Node<V> newNode = new Node<>(value);
            if (isEmpty()) {
                head = newNode;
                tail = head;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        public V dequeue() {
            if (isEmpty()) {
                throw new RuntimeException("链表为空！");
            }
            V ans = head.data;
            head = head.next;
            size--;

            if (head == null) {
                tail = null;
            }
            return ans;
        }
    }
}
