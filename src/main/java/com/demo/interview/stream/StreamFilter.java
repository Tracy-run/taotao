package com.demo.interview.stream;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class StreamFilter {

    public static void main(String [] args) throws Exception{

        List<Employee> timelist = new ArrayList<>();
        timelist.stream().filter(a -> a.getName().equals(11));


    }



    public void filter(){

        List<A> alist = new ArrayList();

        //findFirst 查找第一个满足添加的对象
        Optional<A> first = alist.stream().filter(a -> Integer.valueOf(a.getUserage()) > 18).findFirst();

        //filter过滤属性后重新生成列表
        List<A> collect = alist.stream().filter(a -> "hanmeimei".equals(a.getUsername())).filter(a -> "18".equals(a.getUserage())).collect(Collectors.toList());

        //map 输出单个属性的列表
        List<String> collect1 = alist.stream().map(A::getUserage).collect(Collectors.toList());

        //
        alist.stream().map(A::getUserage).forEach(System.out::println);

        //大小写转换
        alist.stream().map(A::getUsername).map(String::toUpperCase);

        //循环输出满足条件的对象
        alist.stream().filter(a -> !"123".equals(a.getUsername())).forEach(System.out::print);

        //limit属性
        alist.stream().filter(a -> !"123".equals(a.getUsername())).limit(3).forEach(System.out::print);

        //skip 跳过前几条数据
        alist.stream().filter(a -> !"123".equals(a.getUsername())).skip(3).forEach(System.out::print);


        List<String> strList = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");

        //这是讲一个字符串转化为一个字符集合，然后通过流的方式返回
        Stream<Stream<Character>> stream2 = strList.stream().map(StreamApiTest::filterCharacter);
        stream2.forEach((sm) -> {
            sm.forEach(System.out::print);
        });
        //flatMap
//        Stream<Character> stream3 = strList.stream()
//                .flatMap(StreamApiTest::filterCharacter);
//        stream3.forEach(System.out::print);

        //mapToDouble，mapToInt，mapToLong返回的是相对应的流
        List<Double> doubles = Arrays.asList(1D,2D,3D,4D);

        DoubleStream doubleStream = doubles.stream().mapToDouble(d -> d * 2);
        doubleStream.forEach(System.out::print);

//**********************************************************************************************

        //排序
        List<Employee> emps = Arrays.asList(
                new Employee(101, "张三", 28, 9999),
                new Employee(102, "李四", 49, 666),
                new Employee(103, "王五", 38, 333),
                new Employee(104, "赵六", 12, 7777),
                new Employee(105, "田七", 6, 222)
        );


        emps.stream().map(Employee::getName).sorted().forEach(System.out::print);

        emps.stream().sorted((x,y) -> {
            if(x.getAge() == y.getAge()){
                return x.getName().compareTo(y.getName());
            }else{
                return Integer.compare(x.getAge(),y.getAge());
            }
        }).forEach(System.out::print);


        //allmatch  anyMatch  noneMatch
        boolean b = emps.stream().allMatch(e -> e.getName().contains("五"));

        boolean b1 = emps.stream().anyMatch(e -> e.getName().contains("五"));

        boolean b2 = emps.stream().noneMatch(e -> e.getName().contains("五"));


        //******************   reduce
        /*
        一个整数集合，然后通过reduce，从0开始，
        0会被当做x，然后在集合中取1当做y值，然后进行x+y操作返回1，然后在把1当做x值，在到集合中取一个2当做y值，
        以此类推，然后求和sum就是等于55
        */
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        list.stream().reduce(0,(x,y) -> x+y);

        //reduce
        Optional<Integer> op = emps.stream().map(Employee::getSalary).reduce(Integer::sum);


        /*
        toList List<T> 把流中元素收集到List
        toSet Set<T> 把流中元素收集到Set
        toCollection Collection<T> 把流中元素收集到创建的集合
        counting Long 计算流中元素的个数
        summingInt Integer 对流中元素的整数属性求和
        averagingInt Double 计算流中元素Integer属性的平均值
        summarizingInt IntSummaryStatistics 收集流中Integer属性的统计值
        */

        //list存储元素
        List<String> list1 = emps.stream().map(Employee::getName).collect(Collectors.toList());
        list1.forEach(System.out::print);

        //set存储单个元素集合
        Set<String> set = emps.stream().map(Employee::getName).collect(Collectors.toSet());
        set.forEach(System.out::print);

        //求和
        Integer aa = emps.stream().collect(Collectors.summingInt(Employee::getSalary));
        //求平均
        Double aveg = emps.stream().collect(Collectors.averagingInt(Employee::getSalary));
    }



    class A{
        private String username;
        private String userage;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserage() {
            return userage;
        }

        public void setUserage(String userage) {
            this.userage = userage;
        }
    }
    class Employee{

        private Integer id;
        private String name;
        private Integer age;
        private Integer salary;

        public Employee(Integer id, String name, Integer age, Integer salary) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.salary = salary;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Integer getSalary() {
            return salary;
        }

        public void setSalary(Integer salary) {
            this.salary = salary;
        }
    }

    static class StreamApiTest{

        public static <R> R filterCharacter(String s) {
            return null;
        }
    }
}