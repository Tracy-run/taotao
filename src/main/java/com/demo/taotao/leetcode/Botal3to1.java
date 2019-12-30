package com.demo.taotao.leetcode;

import java.util.Scanner;

/**
 * 瓶子3换1
 */
public class Botal3to1 {

    public static void main(String [] args){
        Scanner scan = new Scanner(System.in);
        while(scan.hasNext()){
            int n = scan.nextInt();
            if(n == 0){
                break;
            }
            System.out.println(sumTotal(n));
        }
    }

    public static int sumTotal(int n){

        if(n<3){
            return 0;
        }
        if(n == 3){
            return 1;
        }
        int temp =0;
        int num = 0;
        while(n > 2){
            temp = n%3;//空瓶
            num += n/3;//实瓶
            n = temp + n/3;//空瓶 + 本次实瓶
            if (n == 2) {
                num++;
            }
        }

        return num;
    }

}
