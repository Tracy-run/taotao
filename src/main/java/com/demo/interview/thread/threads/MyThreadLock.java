package com.demo.interview.thread.threads;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * 自己改自己的 线程互不影响
 *
 * 01，《Java编程学习第一季》：av35556299
 * 02，《Java编程学习第二季》：av37316788
 * 03，《Java编程学习第三季》：av37317551
 * 04，《Java编程学习第四季》：av38308449
 * 05，《JDBC编程和MySQL数据库》：av37325712
 * 06，《Web前端第一季（HTML）》：av35875257
 * 07，《Web前端第三季（JavaScript）》：av37383464
 * 08，《Web前端第四季（JQuery）》：av38513367
 * 09，《JavaWeb第一季基础（JSP和Servlet）》：av37398251
 * 10，《JavaWeb第二季进阶》：av37398729
 * 11，《3小时学会使用Maven构建项目》：av38517296
 * 12，《SSH框架第一季 - Struts入门》：av38472605
 * 13，《SSH框架第二季 - Hibernate入门》：av38476142
 * 14，《SSM框架第一季 - Mybatis入门》：av38513367
 * 15，《SSM框架第二季 - Spring入门》：av38516969
 * 16，《Spring Boot快速入门》：av38356979
 * 17，《手把手教你使用Cropper》：av38512574
 *
 * https://www.bilibili.com/video/av38516969/?spm_id_from=333.788.b_636f6d6d656e74.44
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyThreadLock.class)
public class MyThreadLock {


    static ThreadLocal<Person> tl = new ThreadLocal<Person>();


    public static void main(String[] args) {

        new Thread(() ->{
            try {
                TimeUnit.SECONDS.sleep(2);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(tl.get());
        }).start();

        new Thread(() ->{
            try{
                TimeUnit.SECONDS.sleep(1);
            }catch (Exception e){
                e.printStackTrace();
            }
            tl.set(new Person());
        }).start();

    }


}

class Person{
    String name = "zhangsan";
}