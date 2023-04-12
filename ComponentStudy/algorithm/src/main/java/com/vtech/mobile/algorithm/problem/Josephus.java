package com.vtech.mobile.algorithm.problem;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Josephus {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {

        // 默认是小根堆
        PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // 后一个元素减去前一个元素是大根堆
                // 正常顺序就是小根堆
                return o1-o2;
            }
        });
        queue.add(2);
        queue.add(4);
        queue.add(6);
        queue.add(0);
        System.out.println(queue.peek());

    }

    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    // 约瑟夫环 环形单链表
    // 循环报数 报到m的人就自杀
    // 时间复杂度 O(n*m)

    /**
     * @param head
     * @param m
     * @return 最终生存下来的节点
     */
    public static Node josephus(Node head, int m) {
        if (head == null || head.next == head || m < 1) {
            return head;
        }

        Node last = head;
        while (last.next != head) {
            last = last.next;
        }
        int count = 0;
        while (head != last) { // 直到仅剩一个人
            if (++count == m) {
                last.next = head.next;
                count = 0;
            } else {
                last = last.next;
            }
        }
        return head;
    }


}
