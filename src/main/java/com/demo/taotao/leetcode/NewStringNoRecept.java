package com.demo.taotao.leetcode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NewStringNoRecept {


    public static void main(String args []){
//        Scanner scan = new Scanner(System.in);
//        while(scan.hasNext()){
//            String temp = scan.nextLine();
//            char [] c = temp.toCharArray();
//            StringBuffer sb = new StringBuffer();
//            Set<Character> set = new HashSet<>();
//            for(int i =0;i<c.length;i++){
//                if(set.add(c[i])){
//                    sb.append(c[i]);
//                }
//            }
//        }
        String date = LocalDateTime.now().toString();
        System.out.println(date.toString());

    }
}
