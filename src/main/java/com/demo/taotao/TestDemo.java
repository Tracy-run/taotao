package com.demo.taotao;

public class TestDemo {

    public static void main(String [] args) {
        char [] str = new char[]{'a','b','c','d','e'};
        helper(0, str);
    }

    public static void helper(int index, char [] str) {
        if (str == null || index >= str.length) {
            return;
        }
        helper(index + 1, str);
        System.out.print(str[index]);
    }
}
