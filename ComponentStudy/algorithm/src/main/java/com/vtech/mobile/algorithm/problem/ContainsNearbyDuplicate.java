package com.vtech.mobile.algorithm.problem;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ContainsNearbyDuplicate {

    public static void main(String[] args) {

        int[] arr = {1,1,2};
        System.out.println(containsNearbyDuplicate(arr,3));

        HashSet<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()){
            Integer next = iterator.next();
            System.out.println(next);
        }
    }

    /**
     * 给定一个整数数组和一个整数k,判断数组中是否存在两个不同的索引i和j,使得 nums[i] == nums[j],
     * 并且i和j的差的绝对值最大为k
     * @param nums
     * @param k
     * @return
     */
    public static boolean containsNearbyDuplicate(int[] nums, int k){
        if (nums == null||nums.length<1 || k<0){
            return false;
        }
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; ++i) {
            if (set.contains(nums[i])) return true;
            set.add(nums[i]);
            if (set.size() > k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }
}
