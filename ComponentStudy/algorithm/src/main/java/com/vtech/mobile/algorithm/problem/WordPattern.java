package com.vtech.mobile.algorithm.problem;

import java.util.HashMap;
import java.util.HashSet;

public class WordPattern {

    public static void main(String[] args) {

        String pattern1 = "abba";
        String s1 = "dog cat cat dog";
        System.out.println(wordPattern1(pattern1, s1));

        String pattern2 = "abba";
        String s2 = "dog cat cat fish";
        System.out.println(wordPattern1(pattern2, s2));

        String pattern3 = "aaaa";
        String s3 = "dog cat cat dog";
        System.out.println(wordPattern1(pattern3, s3));

        String pattern4 = "abba";
        String s4 = "dog dog dog dog";
        System.out.println(wordPattern1(pattern4, s4));

        System.out.println("Second method : "+wordPattern2(pattern4,s4));
    }

    public static boolean wordPattern2(String pattern, String s) {
        String[] strs = s.split(" ");
        if (pattern.length() != strs.length) return false;
        HashMap<Character, String> map = new HashMap<>();
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            // 如果当前key存在，就判断value是否与当前单词匹配
            if (map.containsKey(c)) {
                if (!map.get(c).equals(strs[i])) {
                    return false;
                }
            } else {
                // 如果map不存在这个key,也就是说是新key,需要添加到map中
                // 再查看str[i]这个value值是否和别的key对应过
                // 如果对应过就不符合双向连接
                if (set.contains(strs[i])) {
                    return false;
                }
                map.put(c, strs[i]);
                set.add(strs[i]);
            }
        }
        return true;
    }

    public static boolean wordPattern1(String pattern, String s) {
        String[] mark = new String[26];
        String[] str = s.split(" ");
        if (str.length != pattern.length()) {
            return false;
        }
        for (int i = 0; i < str.length; i++) {
            int index = pattern.charAt(i) - 97;
            if (mark[index] == null) {
                mark[index] = str[i];
            } else {
                if (!mark[index].equals(str[i])) {
                    return false;
                }
            }
        }
        // 可能存在相同的value值对应不同的key的情况，如第四个例子
        // 要保证一个key只对应一个value
        for (int i = 0; i < 26; i++) {
            for (int j = i + 1; j < 26; j++) {
                if (mark[i] == null || mark[j] == null) {
                    continue;
                }
                if (mark[i].equals(mark[j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
