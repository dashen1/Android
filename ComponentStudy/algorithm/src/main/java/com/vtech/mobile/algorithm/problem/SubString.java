package com.vtech.mobile.algorithm.problem;

import java.util.HashSet;

public class SubString {

    public static void main(String[] args) {

    }

    /**
     * 最长不含重复字符的子字符串
     * 向右扩张的条件是哈希表中没有出现过这个字符
     * 维护窗口最大值
     *
     * @param s
     * @return
     */
    public static int longestUnRepeatingSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] chars = s.toCharArray();
        HashSet<Character> set = new HashSet<>();
        int r = -1;
        int max = 0;
        for (int i = 0; i < chars.length; i++) {
            if (i != 0) {
                set.remove(chars[i - 1]);
            }
            while (r + 1 < chars.length && !set.contains(chars[r + 1])) {
                set.add(chars[r + 1]);
                r++;
            }
            // 每次向右扩张完需要比较一下
            max = Math.max(max, r - i + 1);
        }
        return max;
    }
}
