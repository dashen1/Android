package com.vtech.mobile.algorithm.problem02;

public class LinkedAsStack {

    public static void main(String[] args) {

    }

    public static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            next = null;
        }
    }

    public static class StackByLinked<V> {
        private Node<V> head;
        private int size;

        public StackByLinked() {
            head = null;
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void push(V value) {
            Node<V> newNode = new Node<>(value);
            if (head != null) {
                newNode.next = head;
            }
            head = newNode;
            size++;
        }

        public V pop() {
            if (head == null) {
                throw new RuntimeException("链表为空!");
            }
            V ans = head.data;
            head = head.next;
            size--;
            return ans;
        }
    }
}
