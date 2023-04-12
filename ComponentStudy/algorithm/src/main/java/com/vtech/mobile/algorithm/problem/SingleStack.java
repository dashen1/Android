package com.vtech.mobile.algorithm.problem;

import java.util.Stack;

public class SingleStack {

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        reverse(stack);
    }

    /** 假设栈的顺序是：4、3、2、1 从上到下
     * 栈的变化情况如下：
     * 第一次：4、3、2、1
     * 第二次：4、3、2
     * 第三次：4、3
     * 第四次：4
     *
     * i 的变化情况  1 > 2 > 3 > 4
     * 最后入栈情况: 4 > 3 > 2 > 1
     * @param stack
     */
    public static void reverse(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return;
        }
        int i = f(stack);
        reverse(stack);
        stack.push(i);
    }

    private static int f(Stack<Integer> stack) {
        int result = stack.pop();
        if (stack.isEmpty()) {
            return result;
        } else {
            int last = f(stack);
            stack.push(result);
            return last;
        }
    }
}
