package com.vtech.mobile.algorithm.problem02;

import java.util.Arrays;
import java.util.Comparator;

public class MeetingArrange {

    public static void main(String[] args) {
        Program pro1 = new Program(0, 1);
        Program pro2 = new Program(1, 3);
        Program pro3 = new Program(1, 2);
        Program pro4 = new Program(2, 3);
        Program[] programs = {pro1, pro2, pro3, pro4};
        System.out.println(bestArrange(programs, 0));
    }

    public static class Program {
        int start;
        int end;

        public Program(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    // 按会议结束最短时间 排序 小根堆
    public static class ProgramComparator implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
            return o1.end - o2.end;
        }
    }

    /**
     * @param programs
     * @param start    当前开始时间
     * @return
     */
    public static int bestArrange(Program[] programs, int start) {
        // 先排序
        Arrays.sort(programs, new ProgramComparator());
        int result = 0;
        // 从左往右遍历所有的会议，当前开始时间start要是小于或者等于当前会议的开始时间，就代表当前会议可以安排
        for (int i = 0; i < programs.length; i++) {
            if (start <= programs[i].start) {
                result++;
                start = programs[i].end;
            }
        }
        return result;
    }
}
