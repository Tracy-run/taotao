package com.demo.taotao.java8.inter;

@FunctionalInterface
public interface FunctionalInterfaceTest {

    //抽象方法只能有一个
    String method(String name);

    default String defaultMethod(String name){

        return method(name);
    }

}
