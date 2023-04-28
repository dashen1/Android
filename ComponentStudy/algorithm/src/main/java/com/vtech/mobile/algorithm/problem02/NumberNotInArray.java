package com.vtech.mobile.algorithm.problem02;

public class NumberNotInArray {

    public static void main(String[] args) {
        int[] arr = {1,3,4,3};
        printNumNotInArray(arr,4);
    }

    public static void printNumNotInArray(int[] arr,int n) {
        if (arr.length< 1) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            int value = arr[i];
            while(arr[value-1]!=value){
                int tmp = arr[value-1];
                arr[value-1] = value;
                value = tmp;
            }
        }
        for (int i = 0; i < arr.length; i++) {
            if(arr[i]!=i+1){
                System.out.println(" "+(i+1));
            }
        }
    }
}
