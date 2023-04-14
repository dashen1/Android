package com.vtech.mobile.algorithm.problem;

public class EqualSubstring {

    public static void main(String[] args) {
        String s1 = "abcd";
        String t1 = "bcdf";
        System.out.println(equalSubstring(s1,t1,3));

        String s2 = "abcd";
        String t2 = "cdef";
        System.out.println(equalSubstring(s2,t2,3));

        String s3 = "abcd";
        String t3 = "acde";
        System.out.println(equalSubstring(s3,t3,0));
    }

    public static int equalSubstring(String s, String t, int maxCost) {
        int len = s.length();
        int[] st = new int[len];
        char[] cs = s.toCharArray();
        char[] ct = t.toCharArray();
        for (int i = 0; i < len; i++) {
            st[i] = Math.abs(cs[i] - ct[i]);
        }
        int l = 0, r = 0, cost = 0, ans = 0;
        while (r < len) {
            cost += st[r++];
            while (cost > maxCost) {
                cost -= st[l++];
            }
            ans = Math.max(ans, r - l);
        }
        return ans;
    }
}
