package com.vtech.mobile.algorithm.problem02;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;
import java.util.PriorityQueue;

public class AlwaysGetMiddleNum {

    public static void main(String[] args) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static class MedianHolder {
        private PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2 - o1);
        private PriorityQueue<Integer> minHeap = new PriorityQueue<>((o1, o2) -> o1 - o2);

        private void modifyTwoHeapSize() {
            if (maxHeap.size() == minHeap.size() + 2) {
                minHeap.add(maxHeap.poll());
            }
            if (minHeap.size() == maxHeap.size() + 2) {
                maxHeap.add(minHeap.poll());
            }
        }

        public void addNumber(int num) {
            if (maxHeap.isEmpty()) {
                maxHeap.add(num);
                return;
            }
            if (maxHeap.peek() >= num) {
                maxHeap.add(num);
            } else {
                if (minHeap.isEmpty()) {
                    minHeap.add(num);
                    return;
                }
                if (minHeap.peek() > num) {
                    maxHeap.add(num);
                } else {
                    minHeap.add(num);
                }
            }
            modifyTwoHeapSize();
        }

        public Integer getMedian() {
            int maxHeapSize = maxHeap.size();
            int minHeapSize = minHeap.size();
            if (maxHeapSize + minHeapSize == 0) {
                return null;
            }
            Integer maxHeapHead = maxHeap.peek();
            Integer minHeapHead = minHeap.peek();
            // & 只有 1&1时才为1 说明是奇数
            if (((maxHeapSize + minHeapSize) & 1) == 0) {
                return (maxHeapHead + minHeapHead) / 2;
            }
            return maxHeapHead > minHeapHead ? maxHeapHead : minHeapHead;
        }

    }
}
