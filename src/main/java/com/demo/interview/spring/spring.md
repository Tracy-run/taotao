#一：spring的基本用法：
##1，关于spring容器：
spring容器是Spring的核心，该 容器负责管理spring中的java组件，
ApplicationContext ctx  = new ClassPathXmlApplicationContext("bean.xml");//这种方式实例化容器，容器会自动预初始化所有Bean实例
ctx.getBean("beanName");
ApplicationContext 实例正是Spring容器。
ApplicationContext容器默认会实例化所有的singleton Bean
Spring容器并不强制要求被管理组件是标准的java****bean。
 
##2，Spring的核心机制：依赖注入。
不管是依赖注入(Dependency Injection)还是控制反转(Inversion of Conctrol)，其含义完全相同：
当某个java实例(调用者)需要调用另一个java实例(被调用者)时，传统情况下，通过调用者来创建被调用者的实例，通常通过new来创建，
而在依赖注入的模式下创建被调用者的工作不再由调用者来完成，因此称之为"控制反转"；创建被调用者实例的工作通常由Spring来完成，然后注入调用者，所以也称之为"依赖注入"。
 
##3，依赖注入一般有2中方式：
设置注入：IoC容器使用属性的setter方式注入被依赖的实例。<property name="" ref="">
构造注入：IoC容器使用构造器来注入被依赖的实例。<constructor-arg ref="">
配置构造注入的时候<constructor-arg>可以配置index属性，用于指定该构造参数值作为第几个构造参数值。下表从0开始。
 
##4，Spring容器和被管理的bean：
Spring有两个核心接口：BeanFactory和ApplicationContext，其中ApplicationContext是BeanFactory的子接口。他们都可以代表Spring容器。
Spring容器是生成Bean实例的工厂，并管理Spring中的bean，bean是Spring中的基本单位，在基于Spring的java EE工程，所有的组件都被当成bean处理。
包括数据源、Hibernate的SessionFactory、事务管理器。
###①Spring容器：Spring最基本的接口就是BeanFactory，
BeanFactory有很多实现类，通常使用XmlBeanFactory，但是对于大部分的javaEE应用而言，推荐使用ApplictionContext，它是BeanFactory的子接口，
ApplictionContext的实现类为FileSystemXmlApplicationContext和ClassPathXmlApplicationContext
FileSystemXmlApplicationContext：基于文件系统的XML配置文件创建ApplicationContext；
ClassPathXmlApplicationContext：基于类加载路径下的xml配置文件创建ApplicationContext。
###②ApplicationContext的事件机制，
ApplicationContext事件机制是基于观察者设计模式实现的。通过ApplicationEvent类和ApplicationListener接口，
其中ApplicationEvent：容器事件，必须由ApplicationContext发布；
ApplicationListener：监听器，可有容器内的任何监听器Bean担任。
###③容器中bean的作用域：
singleton：单例模式，在整个Spring IoC容器中，使用singleton定义的bean将只有一个实例；
prototype：原型模式，每次通过容器的getBean方法获取prototype定义的Bean时，都将产生一个新实例；
request：对于每次HTTP请求中，使用request定义的bean都将产生一个新实例，只有在web应用程序使用Spring时，该作用域才有效；
session：同理
global session：同理
        注意：request和session作用域只在web应用中才生效，并且必须在web应用中增加额外的配置才会生效，为了让request，session两个作用域生效，必须将HTTP请求对象绑定到为该请求提供服务的线程上，这使得具有request和session作用域的Bean实例能够在后面的调用链中被访问。
        当支持Servlet2.4及以上规范的web容器时，我们可以在web应用的web.xml增加如下Listener配置，该Listener负责为request作用域生效：
<listener>
    <listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>
</listener>
        如果仅使用了支持Servlet2.4以前规范的web容器，则该容器不支持Listener规范，故无法使用这种配置，可以使用Filter配置方式，我们可以在web应用的web.xml增加如下Filter配置：
<filter>
    <filter-name>requestContextFilter</filter-name>
    <filter-class>org.springframework.web.filter.RequestContextFilter</filter-class>
</filter>
 <filter-mapping>
     <filter-name>requestContextFilter</filter-name>
      <url-pattern>/*</url-pattern>  
 </filter-mapping>
再如下面的代码：
<bean id="p" class="lee.Person" scope="request"/>
这样容器就会为每次HTTP请求生成一个lee.Person的实例当该请求响应结束时，该实例也随之消失。
如果Web应用直接使用Spring MVC作为MVC框架，即使用SpringDispatchServlet或DispatchPortlet来拦截所有用户请求，则无需这些额外的配置，因为SpringDispatchServlet或DispatchPortlet已经处理了所有和请求有关的状态处理。
 
 
###④获取容器的引用：
通常情况下：
Bean无需访问Spring容器，而是通过Spring容器访问的，即使 需要手动访问Spring容器，程序也已通过类似下面的代码获取Spring容器 的引用。
ApllicationContext cts = ClassPathApplalicationContext("bean.xml");
但在一些极端的情况下，可能Bean需要访问Spring容器。Spring提供另一种方法访问Spring容器：
实现BeanFactoryAware接口的Bean，拥有访问Spring容器的能力，实现BeanFactoryAware的Bean被容器实例化后，会拥有一个引用指向创建他的BeanFactory。BeanFactoryAware只有一个方法setBeanFactory(BeanFactory beanFactory)该参数指向创建他的BeanFactory。
缺点：污染了代码，使代码与Spring接口耦合在一起，因此没有特别的必要，建议不要直接访问容器。
 
##5，Bean实例的创建方式及对应配置：
创建Bean的方法：
①调用构造器创建Bean实例；
②调用静态工厂方法创建Bean；
③调用实例工厂创建Bean。
调用静态工厂方法创建Bean：
class属性是必须的，但此时的class并不是指定Bean实例的实现类而是静态工厂类。采用静态工厂类需要配置如下两个属性：
class静态工厂类的名字；
factory-method工厂方法(必须是静态的)。
如果静态工厂的方法有参数通过<constructor-arg/>元素知道。
调用实例工厂方法创建Bean：
使用实例工厂Bean时class属性无需指定，因Spring容器不会直接实例化该Bean，
创建Bean时需要如下属性：
factory-bean：该属性为工厂Bean的ID；
factory-method：该属性是定实例工厂的工厂方法。
 
##6，入理解Spring容器中的Bean：
抽象Bean：
所有的抽象Bean，就是是定abstract属性为true的Bean，抽象Bean不能被实例化，抽象Bean的价值在于被继承
使用子Bean：
随着应用规模的增大，Spring配置文件的增长速度更快。当应用中的组件越来越多，，Spring中的Bean配置也随之大幅度增加。
就会出现一中现象：有一批配置Bean的信息完全相同，只有少量 的配置不同。怎么解决呢？
这时候就可以用Bean的继承来解决。
注意：子Bean无法从父Bean继承如下属性：
depends-on,aotuwirwe,dependency-check,singleton,scope,lazy-iniyt这些属性总是子Bean定义，或采用默认值。
通过 为一个<bean.../> 元素指定parent属性，即可指定该Bean是一个子Bean。                                                                                                       
Bean继承与java中继承的区别：
Spring中的子bean和父Bean可以是不同类型，但java中的继承则可保证子类是一种特殊的父类；
Spring中的Bean的继承是实例之间的关系，因此只要表现在参数值的延续，而java中的继承是类之间的关系，主要表现为方法、属性之间的延续；
Spring中的子Bean不可以作为父Bean使用，不具备多态性，java中的子类完全可以当成父类使用。
Bean的生命周期：
###①singleton与prototype的区别：
singleton：Spring可以精确的知道该Bean何时被创建、初始化、销毁。对于singleton作用域的Bean，每次客户端请求Spring容器总会返回一个共享的实例。
prototype：Spring容器仅仅负责创建Bean，当容器创建了Bean的实例后，Bean实例完全交给客户端代码管理，容器不在跟踪其生命周期。
每次客户端请求prototype作用域的Bean，都会为他创建一个新的实例，
###②依赖关系注入后的行为：
Spring提供两种方法在Bean全部属性设置成功后执行特定的行为：
使用init-method属性；
该Bean实现InitializingBean接口
第一种方法：使用init-method属性指定某个方法在Bean全部属性依赖关系设置结束后自动执行。使用这种方法不需要将代码与Spring的接口耦合在一起，代码污染少；
第二种方法：实现Initializing接口，该接口有一个方法void afterPropertiesSet() throws Exception,虽然实现次接口一样可以在Bean全部属性设置成功后执行特定的行为，但是污染了代码，是侵入式设计，因此不推荐使用。
注意：如果即采用init-method属性指定初始化方法，又实现InitializingBean接口来指定初始化方法，先执行initializingBean接口中定义的方法，再执行init-method属性指定的方法。
###③Bean销毁之前行为：
与定制初始化相似，Spring也提供两种方式定制Bean实例销毁之前的特定行为，如下：
使用destroy-method属性：
实现DisposableBean接口：
注意：如果即采用destroy-method属性指定销毁之前的方法，又实现DisposableBean接口来指定指定销毁之前的方法，与②类似。
###④default-init-method与default-destroy-method属性，指定了所有的Bean都会执行此方法，而不是单个的Bean。
协调作用域不同步的Bean：
描述：
当Spring容器中作用域不同的Bean相互依赖时，可能出现一些问题:
当两个singleton作用域Bean存在依赖关系时，或当prototype作用依赖singleton作用域的Bean时，通过属性定义依赖关系即可。、
但是，当singleton作用域的Bean依赖prototype作用域Bean时，singleton作用域的Bean只有一次初始化的机会，他的依赖关系也只有在初始化阶段被设置，而他所依赖的prototype作用域的Bean则会不断的产生新的Bean实例。
解决方案：
第一种：部分放弃依赖注入：singleton作用域的Bean每次需要prototype作用域的Bean，则主动向容器请求新的Bean实例。
第二种：利用方法注入。
第一种方案肯定是不好的，代码主动请求新的Bean实例，必然会导致与Spring API耦合，造成代码严重污染。
通常情况下采用第二中方式。
方法注入通常使用lookup方法注入，利用lookup方法注入可以让Spring容器重写容器中Bean的抽象方法或具体方法，返回查找容器中的其他 Bean，被查找的Bean通常是non-singleton Bean(尽管也可以是singleton).
如：public class SteelAxe implements Axe{
        //每执行一次加一
        private int count;
        public String  chop(){
            return ++count;
    }
}
 
public abstract class Chinese implements Perosom{
        private Axe axe;
//定义一个抽象方法，该方法将由Spring负责实现
        public abstract Axe createAxe();
        public voidsetAxe(Axe axe){
                this axe = axe;
        }
        public Axe getAxe(){
                return axe;
        }
}
在Spring配置文件中配置：
<bean id="steelAxe" class="...SteelAxe" scope="prototype"></bean>
 
<bean id="chinese" class="..Chinese" >
  < lookup-mehtod name="createAxe" bean="steelAxe">
    <property name="axe" ref="steelAxe"/>
</bean>
容器中的工厂Bean：
此处的工厂Bean与前面介绍的实例工厂方法创建Bean、静态工厂创建Bean有所区别：
       前面的那些工厂是标准的工厂模式，Spring只是负责调用工厂方法来创建Bean实例；
       此处工厂Bean是Spring的一种特殊Bean，这种工厂Bean必须实现FactoryBean接口。
FactoryBean接口是工厂Bean标准的工厂Bean的接口，实现该接口的Bean只能当工厂Bean使用，当我们将工厂Bean部署在容器中，并通过getBean()方法来获取工厂Bean,容器不会返回FactoryBean实例而是FactoryBean的产品。
FactoryBean提供了三个方法：
Object getObject();
Class getObjectType();
boolean isSingleton();
如：
 public class PersonFactory implements FactoryBean{
    Person p = null;
    public Object getObject() throws Exception{
        if(p==null){
            p  = new Chinense();
            return p;
        }
    }
    public Class getObjectType(){
        return Chinese.class;
     }
 
    public boolean isSingleton(){
        return true;
    }
}
 
<!--配置一个FactoryBean，和普通的Bean一样-->
<bean id="chinese" class=""/>
 
 
        public static void main(String args[]){\
                //以classpth下的bean.xml创建Reource对象
                ClassPathResource re = new ClasspathResource("bean.xml");
                //创建BeanFactory
                XmlBeanFactory factory = new XmlBeanFactory(re);
                Person p = (Person)factory.getBean("chinese");
                //如需要获取FactoryBean本身则应该在bean id前加&
                Person p = (Person)factory.getBean("&chinese");
        }
对于初学者可能无法体会到工厂bean的作用，实际上，FactoryBean是Spring中非常有用的接口。例如：TransationProxyFactroyBean,这个工厂转为目标Bean创建事务代理.

## 7,深入理解依赖关系配置
组件与组件之间的耦合，采用依赖注入管理，但是普通的javabean属性值，应直接在代码里设置。
对于singleton作用域的bean，如果没有强制取消其预初始化行为，系统会在创建Spring容器时预初始化所有的singleton作用域的bean，与此同时，该bean依赖的bean也一起被实例化。
        BeanFactory与ApplicationContext实例化容器中的bean的时机不同，前者等到程序需要Bean实例才创建Bean
后者会预初始化容器中的所有Bean。
因为采用ApplicationContext作为Spring的容器，创建容器时，会创建容器中所有singleton作用域的所有bean，因此可能需要更多的系统资源，但是一旦创建成功。应用后面的 响应速度会很快，因此，对于普通的javaEE而言 ，建议使用ApplicationContext作为Spring的容器。
Bean实例4中属性值的设置：
value；ref；bean；list、set、map、props
①设置普通属性值value，略；
②配置合作者Bean   ref
可以为ref元素指定两个属性：bena、Local
bean：引用在不同一份XML配置文件中的其他Bean实例的ID属性值；
Local：引用同一份XML配置文件的其他Beanid属性值。
也可以不配置以上两个属性。
③组合属性名称：
public class  A{
    private Person p = new Person();
    set/get....
}
Spring配置文件
<bean id="a" class="A">
    <property name="p.name" value="aaa"/>
</bean>
④注入嵌套Bean：
<bean id="" class="">
       < property name="">
                //属性为嵌套Bean 不能由Spring容器直接访问，因此没有id属性
                <bean class="..."/>
        </property>
</bean>
⑤注入集合值：
<list>
<value></value>
<value></value>
</list>
 
<map>
//每一个entry配置一个key-value对
<entry>
    <key>
        <value>.</value>
    </key>
    <value></value>
</entry>
</map>
 
<set>
    <value></value>
    <bean></bean>
    <ref local=""/>
</set>
 
<props>
    <prop key="">.....</prop>
    <prop key="">.....</prop>
</props>
⑥注入方法返回值：
public class ValueGenrator{
    public int getValue(){
        return 6;
    }
    public static int getStaticValue(){
        return 9;
    }
}
 
<bean id="valueGenrator" class="lee.ValueGenrator"/>
<bean id="son1" class="Son">
    <property name="age">
        <bean class="org.springframework.bean.factory.congfig.MethodInvokignFactoryBean">
//配置非静态方法
            <property name="targetObject" ref="valueGenrator"/>
//配置静态方法
<!--
            <property name="targetClass" value="lee.ValueGenrator"/>
-->
            <property name="targetMehtod" value="getStaticValue/>
</property>
</bean>
##8,强制初始化Bean：
Spring有一个默认的规则，总是先初始化主调Bean，然后在初始化依赖Bean。
为了指定Bean在目标Bean之前初始化，可以使用depends-on属性
##9，自动装配：
Spring能自动装配Bean与Bean之间的依赖关系，即使无需使用ref显式指定依赖Bean。
Spring的自动装配使用autowire属性指定，每一个<bean/>元素都可以指定autowire属性，也就是说在Spring容器中完全可以让某些Bean自动装配，而某些Bean不没使用自动装配。
自动装配可以减少配置文件的工作量，但是降低了依赖关系的透明性和依赖性。
使用autowire属性自动装配，autowire属性可以接受如下几个值 ：
no：不使用自动装配。这是默认配置。
byName：根据属性自动装配，BeanFactory会查找容器中所有的Bean，找出id属性与属性名同名的Bean来完成注入。如果没有找到匹配的Bean，Spring则不会进行任何注入。
byType：根据属性类型自动装配，BeanFactroy会查找容器中所有的 Bean，如果一个正好与依赖属性类型相同的Bean，就会自动注入这个属性。
如果有多个这样的Bean则会抛出异常。如果没有这样 的Bean则什么也不会发生，属性不会被设置。
constructor：与byType类似，区别是用于构造注入的参数，如果BeanFactory中不是恰好有一个Bean与构造器参数类型相同。则会抛出异常。
autodetect：BeanFactory会根据Bean内部的结构，决定使用constructor或byType，如果找到一个缺省的构造器，就会应用byType。
注意：对于大型的应用而言，不鼓励使用自动装配，
##10，依赖检查：
Spring提供一种依赖检查的功能，可以防止出现配置手误，或者其他情况的错误。
使用依赖检查可以让系统判断配置文件的依赖关系注入是否完全有效。
使用依赖检查，可以保证Bean的属性得到了正确的设置，有时候，某个Bean的特定属性并不需要设置值，或者某些属性已有默认值，此时采用依赖检查就会出现错误，该Bean就不应该采用依赖检查，幸好Spring可以为不同的Bean单独指定依赖检查的行为，Spring提供dependency-chech属性来配置依赖检查，当然也可以指定不同的检查依赖策略。
该属性有如下值：
none：不进行依赖检查，没有指定值的Bean属性仅仅是没有设置值，这是默认值。
simple：对基本类型和集合(除了合作者Bean)进行依赖检查。
objects：仅对合作者Bean进行依赖检查。
all：对合作者Bean、基本数据类型全部进行依赖检查。
 
public class Chinese implements Person{
        private Axe axe;
        private int age = 30;
            //implements method 
 
        public void setAge(int age){
            this.age = age;
        }
        public int getAge(){
            return age
        }
}
<bean id="axe" class="StoneAxe"/>
<bean id="chinese" class="Chinese" dependency-check="all">
    <property name="axe" ref="axe"/>
</bean>