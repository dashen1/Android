package com.vtech.mobile.algorithm.problem;

import java.util.HashMap;

public class LRU {

    public static void main(String[] args) {

    }

    public static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> last;
        public Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class NodeDoubleLinkedList<K, V> {
        private Node<K, V> head;
        private Node<K, V> tail;

        public NodeDoubleLinkedList() {
            head = null;
            tail = null;
        }

        public void addNode(Node<K, V> newNode) {
            if (newNode == null) {
                return;
            }
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.last = tail;
                tail = newNode;
            }
        }

        private void moveToTail(Node<K, V> node) {
            if (tail == node) {
                return;
            }
            if (this.head == node) {
                this.head = head.next;
                this.head.last = null;
            } else {
                node.last.next = node.next;
                node.next.last = node.last;
            }
            node.last = this.tail;
            node.next = null;
            this.tail.next = node;
            this.tail = node;
        }

        public Node<K, V> removeHead() {
            if (head == null) {
                return null;
            }
            Node<K, V> res = this.head;
            // 只有一个节点的情况
            if (this.head == this.tail) {
                this.head = null;
                this.tail = null;
            } else {// 有两个或两个以上的节点
                this.head = res.next;
                res.next = null;
                this.head.last = null;
            }
            return res;
        }
    }

    public static class MyCache<K, V> {
        private HashMap<K, Node<K, V>> keyModeMap;
        private NodeDoubleLinkedList<K, V> nodeList;
        private final int capacity;

        public MyCache(int capacity) {
            this.capacity = capacity;
            keyModeMap = new HashMap<>();
            nodeList = new NodeDoubleLinkedList<>();
        }

        public V get(K key) {
            if (keyModeMap.containsKey(key)) {
                Node<K, V> res = keyModeMap.get(key);
                nodeList.moveToTail(res);
                return res.value;
            }
            return null;
        }

        // 可能是新增，也可能是更新
        public void set(K key, V value) {
            if (keyModeMap.containsKey(key)) {// 更新
                Node<K, V> node = keyModeMap.get(key);
                node.value = value;
                nodeList.moveToTail(node);
            } else {// 新增
                Node<K, V> newNode = new Node<>(key, value);
                keyModeMap.put(key, newNode);
                nodeList.addNode(newNode);
                if (keyModeMap.size() == capacity + 1) {

                }
            }
        }

        private void removeMostUnusedCache() {
            Node<K, V> removeNode = nodeList.removeHead();
            keyModeMap.remove(removeNode.key);
        }
    }
}
