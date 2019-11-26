package com.demo.interview.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class SpringTest {

    /*
    https://www.cnblogs.com/shijiaoyun/p/7458341.html

    1，关于spring容器：
    spring容器是Spring的核心，该 容器负责管理spring中的java组件，
    ApplicationContext ctx  = new ClassPathXmlApplicationContext("bean.xml");//这种方式实例化容器，容器会自动预初始化所有Bean实例
    ctx.getBean("beanName");
    ApplicationContext 实例正是Spring容器。
    ApplicationContext容器默认会实例化所有的singleton Bean
    Spring容器并不强制要求被管理组件是标准的javabean。

    2，Spring的核心机制：依赖注入。
    不管是依赖注入(Dependency Injection)还是控制反转(Inversion of Conctrol)，其含义完全相同：
    当某个java实例(调用者)需要调用另一个java实例(被调用者)时，传统情况下，通过调用者来创建被调用者的实例，通常通过new来创建，
    而在依赖注入的模式下创建被调用者的工作不再由调用者来完成，因此称之为"控制反转"；创建被调用者实例的工作通常由Spring来完成，然后注入调用者，所以也称之为"依赖注入"。

    3，依赖注入一般有2中方式：
    设置注入：IoC容器使用属性的setter方式注入被依赖的实例。<property name="" ref="">
    构造注入：IoC容器使用构造器来注入被依赖的实例。<constructor-arg ref="">
    配置构造注入的时候<constructor-arg>可以配置index属性，用于指定该构造参数值作为第几个构造参数值。下表从0开始。

    4，Spring容器和被管理的bean：
    Spring有两个核心接口：BeanFactory和ApplicationContext，其中ApplicationContext是BeanFactory的子接口。他们都可以代表Spring容器。
    Spring容器是生成Bean实例的工厂，并管理Spring中的bean，bean是Spring中的基本单位，在基于Spring的java EE工程，所有的组件都被当成bean处理。
    包括数据源、Hibernate的SessionFactory、事务管理器。
    ①Spring容器：Spring最基本的接口就是BeanFactory，
        BeanFactory有很多实现类，通常使用XmlBeanFactory，但是对于大部分的javaEE应用而言，推荐使用ApplictionContext，它是BeanFactory的子接口，
        ApplictionContext的实现类为FileSystemXmlApplicationContext和ClassPathXmlApplicationContext
        FileSystemXmlApplicationContext：基于文件系统的XML配置文件创建ApplicationContext；
        ClassPathXmlApplicationContext：基于类加载路径下的xml配置文件创建ApplicationContext。
    ②ApplicationContext的事件机制，
        ApplicationContext事件机制是基于观察者设计模式实现的。通过ApplicationEvent类和ApplicationListener接口，
        其中ApplicationEvent：容器事件，必须由ApplicationContext发布；
        ApplicationListener：监听器，可有容器内的任何监听器Bean担任。
    ③容器中bean的作用域：
        singleton：单例模式，在整个Spring IoC容器中，使用singleton定义的bean将只有一个实例；
        prototype：原型模式，每次通过容器的getBean方法获取prototype定义的Bean时，都将产生一个新实例；
        request：对于每次HTTP请求中，使用request定义的bean都将产生一个新实例，只有在web应用程序使用Spring时，该作用域才有效；
        session：同理
        global session：同理



     */






    BeanFactory bean = null;

    //ApplictionContext的实现类
    FileSystemXmlApplicationContext cont = null;
    ClassPathXmlApplicationContext text = null;

    ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");;
    ApplicationContext cts = null;

    ClassPathResource re = new ClassPathResource("bea.xml");
    //创建beanFactory
    XmlBeanFactory factory = new XmlBeanFactory(re);




}
