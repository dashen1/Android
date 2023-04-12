package com.vtech.mobile.algorithm.problem;

public class Fabonaci {

    public static void main(String[] args) {
        System.out.println(fabonaci(3));
    }

    public static int fabonaci(int i){
        if (i<0){
            return 0;
        } else if(i==1||i==2){
            return 1;
        }else {
            return fabonaci(i-1)+fabonaci(i-2);
        }
    }
}
