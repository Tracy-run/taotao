## 阿里一面

##   1.创建对象的方式
        
    一般共2种
    1.new 类名
    2.反射
    3.使用Constructor类的newInstance方法
    4.clone


##   2.session和cookie的区别

    1.Cookie 以文本文件格式存储在浏览器中，session存储在服务端它存储了限制数据量
    2.session 的安全性更高
    3.设置cookie时间可以使cookie过期。但是使用session-destory()，我们将会销毁会话。
    
        
##   3.session存储的路径？

###
    1.分布式缓存memcache
    2.redis
    3.1 粘性Session处理方式  
        不同的Tomcat指定访问不同的主memcached。多个memcached之间信息是同步的，能主从备份和高可用,
        用户访问时，首先在Tomcat中创建Session，然后将Session复制一份放到它对应的memcached上。memcached只起到备份左右，
        读写都在Tomcat上。当某一个Tomcat挂掉之后，集群将用户的访问定位到备Tomcat上，然后根据cookie中存储的SessionID找到Session，
        找不到时，再去相应的memcached上去寻找Session，找到之后将其复制到Tomcat上。
    3.2 非粘性Session处理方式
        原理：memcached做主从复制，写入Session在从memcached服务器上，读取Session从主memcached，Tomcat本身不存储Session。
        可容错，Session实时响应。
    
    
    
    
##   4.分表分库的方法，用什么字段？如何保障均衡



##   5.数据库的隔离级别哪几种
    
    1.dirty read 脏读
        事务A访问了数据库，它干了一件事情，往数据库里加上了新来的牛人的名字，但是没有提交事务。      
        insert into T values (4, '牛D'); 这时，来了另一个事务B，他要查询所有牛人的名字。        
        select Name from T; 这时，如果没有事务之间没有有效隔离，那么事务B返回的结果中就会出现“牛D”的名字。

    2.unrepeatable read不可重复读
        事务A访问了数据库，他要查看ID是1的牛人的名字，于是执行了        
        select Name from T where ID = 1;        
        这时，事务B来了，因为ID是1的牛人改名字了，所以要更新一下，然后提交了事务。        
        update T set Name = '不牛' where ID = 1;        
        接着，事务A还想再看看ID是1的牛人的名字，于是又执行了        
        select Name from T where ID = 1;
        结果，两次读出来的ID是1的牛人名字竟然不相同
    
    3.phantom problem 幻读
        事务A访问了数据库，他想要看看数据库的牛人都有哪些，于是执行了        
        select * from T;        
        这时候，事务B来了，往数据库加入了一个新的牛人。        
        insert into T values(4, '牛D');        
        这时候，事务A忘了刚才的牛人都有哪些了，于是又执行了。        
        select * from T;        
        结果，第一次有三个牛人，第二次有四个牛人。        
        相信这个时候事务A就蒙了，刚才发生了什么


    •读未提交（Read Uncommitted）
        就是可以读到未提交的内容。        
        因此，在这种隔离级别下，查询是不会加锁的，也由于查询的不加锁，所以这种隔离级别的一致性是最差的，可能会产生“脏读”、“不可重复读”、“幻读”。

    •读提交（Read Committed）
        就是只能读到已经提交了的内容。        
        这是各种系统中最常用的一种隔离级别，也是SQL Server和Oracle的默认隔离级别。这种隔离级别能够有效的避免脏读，但除非在查询中显示的加锁
        
        select * from T where ID=2 lock in share mode;        
        
        select * from T where ID=2 for update
    
    •可重复读（Repeated Read）
        就是专门针对“不可重复读”这种情况而制定的隔离级别，自然，它就可以有效的避免“不可重复读”。而它也是MySql的默认隔离级别
    
    •串行化（Serializable）
        这是数据库最高的隔离级别，这种级别下，事务“串行化顺序执行”，也就是一个一个排队执行
        这种级别下，“脏读”、“不可重复读”、“幻读”都可以被避免，但是执行效率奇差，性能开销也最大


##   6.中间件的种类
    
    常用中间件的分类：
    
    1.远程过程调用和对象访问中间件：主要解决分布式环境下应用的互相访问问题，
    2.消息中间件：解决应用之间的消息传递，解耦，异步的问题。
    3.数据访问中间件：主要解决应用访问数据库的共性问题的组件。
    
    
    6.1 activeMq
        https://www.open-open.com/project/5041488406300775948.html
    
        ActiveMQ是一个开放源码基于Apache 2.0 licenced 发布并实现了JMS 1.1。它能够与Geronimo，轻量级容器和任Java应用程序无缝的给合。
    
        ActiveMQ特性:        
        多种语言和协议编写客户端。语言：
        java,C,C++,C#,Ruby,Perl,Python,PHP。应用协议：OpenWire，Stomp REST， WS Notification， XMPP，AMQP
        完全支持JMS1.1和J2EE1.4规范（持久化，XA消息，事务）
        虚拟主题、组合目的、镜像队列。
        
    6.2 RabbitMQ
        https://www.open-open.com/project/5041490619794283225.html
        
        这是Play! Framework开发框架的一个扩展模块。用于生产和消费RabbitMQ消息。
        RabbitMQ是一个开源的AMQP实现，服务器端用Erlang语言编写。用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。
        
        RabbitMQ特性:
        支持多种客户端，如：Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript等
        AMQP的完整实现（vhost、Exchange、Binding、Routing Key等）
        事务支持/发布确认
        消息持久化
        
    6.3 Message Queue
        https://www.open-open.com/project/5041489547448577927.html
    
        Open Message Queue是Sun Java System Message Queue的一个开源版本。Open message queue是一个企业级，可升级，非常成熟的消息服务器。
        它为面向消息的系统集成提供一套完整的JMS（Java Message Service ）实现。由于Open MQ源自Sun的Java Message Queue，
        所以其具有Java System Message Queue拥有的所有特性，功能和性能 。
        
    6.4 Kafka
        https://www.open-open.com/project/5041490600333883253.html
        
        Kafka是一个高吞吐量分布式消息系统。linkedin开源的kafka。 Kafka就跟这个名字一样，设计非常独特。首先，kafka的开发者们认为不需要在内存里缓存什么数据，
        操作系统的文件缓存已经足够完善和强大，只要你不搞随机写，顺序读写的性能是非常高效的。kafka的数据只会顺序append，数据的删除策略是累积到一定程度或者超过一定时间再删除。
        Kafka另一个独特的地方是将消费者信息保存在客户端而不是MQ服务器，这样服务器就不用记录消息的投递过程，每个客户端都自己知道自己下一次应该从什么地方什么位置读取消息，
        消息的投递过程也是采用客户端主动pull的模型，这样大大减轻了服务器的负担。Kafka还强调减少数据的序列化和拷贝开销，它会将一些消息组织成Message Set做批量存储和发送，
        并且客户端在pull数据的时候，尽量以zero-copy的方式传输，利用sendfile（对应java里的 FileChannel.transferTo/transferFrom）这样的高级IO函数来减少拷贝开销。
        可见，kafka是一个精心设计，特定于某些应用的MQ系统，这种偏向特定领域的MQ系统我估计会越来越多，垂直化的产品策略值的考虑。
    
        Kafka特性:
        通过O(1)的磁盘数据结构提供消息的持久化，这种结构对于即使数以TB消息存储也能保持长时间的稳定性能。
        高吞吐量：即使是非常普通的硬件Kafka也可以支持每秒数百万的消息。
        Partition、Consumer Group
    
    6.5 JMS
        Java消息服务(Java Message Service) 即JMS
        一个Java平台中关于面向消息中间的API，用于在两个应用程序之间或者分布式系统中发送消息，进行异步通信。

    6.6 AMQP
        AMQP(advanced message queuing protocol) 是一个提供统一消息服务的应用层标准协议，基于此协议的客户端与消息中间件可传递消息，
        并不受客户端/中间件不同产品，不同开发语言等条件的限制。


                ActiveMq    |  RabbitMQ    |    Kafka
            |：-------------|：-------------|----------------：|    
    跨语言   | 支持（JAVA优先）|   语言无关	     |   支持（JAVA优先）
    支持协议 | OpenWire，Stomp，XMPP，AMQP | AMQP | 	 
    优点	    | 遵循JMS规范 安装部署方便	| 继承Erlang天生的并发性，最初用于金融行业，稳定性，安全性有保障  |  依赖zk，可动态扩展节点高性能、高吞吐量、无限扩容消息可指定追溯
    缺点	    | 根据其他用户反馈，会莫名其妙丢失消息。目前重心在下代产品apolle上，目前社区不活跃，对5.X维护较少 | Erlang语言难度较大，不支持动态扩展  |  严格的顺序机制，不支持消息优先级，不支持标准的消息协议，不利于平台迁移
    综合评价	| 适合中小企业级消息应用场景，不适合上前队列的应用场景 | 适用对性能稳定要求高的企业级应用 | 一般应用在大数据日志处理或对实时性（少量延迟），可靠性（少量丢失数据）要求稍低的场景使用
   
    

##   7.不同服务间的调用协议 




##   8.linux中的指令  查询缓存  定位  查看进程 

   ### 1.查进程
        ps命令查找与进程相关的PID号：
        ps a 显示现行终端机下的所有程序，包括其他用户的程序。
        ps -A 显示所有程序。
        ps c 列出程序时，显示每个程序真正的指令名称，而不包含路径，参数或常驻服务的标示。
        ps -e 此参数的效果和指定"A"参数相同。
        ps e 列出程序时，显示每个程序所使用的环境变量。
        ps f 用ASCII字符显示树状结构，表达程序间的相互关系。
        ps -H 显示树状结构，表示程序间的相互关系。
        ps -N 显示所有的程序，除了执行ps指令终端机下的程序之外。
        ps s 采用程序信号的格式显示程序状况。
        ps S 列出程序时，包括已中断的子程序资料。
        ps -t<终端机编号> 指定终端机编号，并列出属于该终端机的程序的状况。
        ps u 以用户为主的格式来显示程序状况。
        ps x 显示所有程序，不以终端机来区分。
    
    最常用的方法是ps aux,然后再通过管道使用grep命令过滤查找特定的进程,然后再对特定的进程进行操作
    
    ps aux | grep program_filter_word,ps -ef |grep tomcat
    
    ps -ef|grep java|grep -v grep 显示出所有的java进程，去处掉当前的grep进程
    
   ### 2.杀进程
       使用kill命令结束进程：kill xxx
       常用：kill －9 324
       Linux下还提供了一个killall命令，可以直接使用进程的名字而不是进程标识号，例如：# killall -9 NAME

   ### 进入到进程的执行文件所在的路径下，执行文件 ./文件名



##   9.MQ的消费者准确读取生产者值的原理
    
    RocketMQ集群的一部分通信：
    
    （1）Broker启动后需要完成一次将自己注册至NameServer的操作；随后每隔30s时间定期向NameServer上报Topic路由信息；
    （2）消息生产者Producer作为客户端发送消息时候，需要根据Msg的Topic从本地缓存的TopicPublishInfoTable获取路由信息。
        如果没有则更新路由信息会从NameServer上重新拉取；
    （3）消息生产者Producer根据（2）中获取的路由信息选择一个队列，（MessageQueue）进行消息发送；Broker作为消息的接收者收消息并落盘存储

    Half(Prepare) Message
    
    指的是暂不能投递的消息，发送方已经将消息成功发送到了 MQ 服务端，但是服务端未收到生产者对该消息的二次确认，
    此时该消息被标记成“暂不能投递”状态，处于该种状态下的消息即半消息。

    消息回查
    
    由于网络闪断、生产者应用重启等原因，导致某条事务消息的二次确认丢失，MQ 服务端通过扫描发现某条消息长期处于“半消息”时，
    需要主动向消息生产者询问该消息的最终状态（Commit 或是 Rollback），该过程即消息回查。    
    
 ###执行流程图
 
    [transaction-execute-flow.png]
    
    发送方向 MQ 服务端发送消息。
    MQ Server 将消息持久化成功之后，向发送方 ACK 确认消息已经发送成功，此时消息为半消息。
    发送方开始执行本地事务逻辑。
    发送方根据本地事务执行结果向 MQ Server 提交二次确认（Commit 或是 Rollback），MQ Server 收到 Commit 状态则将半消息标记为可投递，订阅方最终将收到该消息；MQ Server 收到 Rollback 状态则删除半消息，订阅方将不会接受该消息。
    在断网或者是应用重启的特殊情况下，上述步骤4提交的二次确认最终未到达 MQ Server，经过固定时间后 MQ Server 将对该消息发起消息回查。
    发送方收到消息回查后，需要检查对应消息的本地事务执行的最终结果。
    发送方根据检查得到的本地事务的最终状态再次提交二次确认，MQ Server 仍按照步骤4对半消息进行操作。
    
 NameServer作用   
    
    nameServer顾名思义，在系统中肯定是做命名服务，服务治理方面的工作，功能应该是和zookeeper差不多，据我了解，RocketMq的早期版本确实是使用的zookeeper,后来改为了自己实现的nameserver。
    
    现在来看一下nameServer在RocketMQ中的两个主要作用：
    
    NameServer维护了一份Broker的地址列表和，broker在启动的时候会去NameServer进行注册，会维护Broker的存活状态.
    
    NameServer维护了一份Topic和Topic对应队列的地址列表,broker每次发送心跳过来的时候都会把Topic信息带上.
    
  ###结合部署结构图，描述集群工作流程：
    
   >1，启动Namesrv，Namesrv起来后监听端口，等待Broker、Produer、Consumer连上来，相当于一个路由控制中心。
    2，Broker启动，跟所有的Namesrv保持长连接，定时发送心跳包。心跳包中包含当前Broker信息(IP+端口等)以及存储所有topic信息。
    注册成功后，namesrv集群中就有Topic跟Broker的映射关系。
    3，收发消息前，先创建topic，创建topic时需要指定该topic要存储在哪些Broker上。也可以在发送消息时自动创建Topic。
    4，Producer发送消息，启动时先跟Namesrv集群中的其中一台建立长连接，并从Namesrv中获取当前发送的Topic存在哪些Broker上，
    然后跟对应的Broker建长连接，直接向Broker发消息。
    5，Consumer跟Producer类似。跟其中一台Namesrv建立长连接，获取当前订阅Topic存在哪些Broker，然后直接跟Broker建立连接通道，
   > 开始消费消息。
    
https://www.jianshu.com/p/d5da161efc33

https://blog.csdn.net/lirenzuo/article/details/81275785

https://blog.csdn.net/mr253727942/article/details/52637126

https://blog.csdn.net/javahongxi/article/details/72956608


##   10.设计模式有哪些？
    
    创建型有：
        一、Singleton，单例模式：保证一个类只有一个实例，并提供一个访问它的全局访问点 
        二、Abstract Factory，抽象工厂：提供一个创建一系列相关或相互依赖对象的接口，而无须指定它们的具体类。 
        三、Factory Method，工厂方法：定义一个用于创建对象的接口，让子类决定实例化哪一个类，Factory Method使一个类的实例化延迟到了子类。 
        四、Builder，建造模式：将一个复杂对象的构建与他的表示相分离，使得同样的构建过程可以创建不同的表示。 
        五、Prototype，原型模式：用原型实例指定创建对象的种类，并且通过拷贝这些原型来创建新的对象。 
    行为型有： 
        六、Iterator，迭代器模式：提供一个方法顺序访问一个聚合对象的各个元素，而又不需要暴露该对象的内部表示。 
        七、Observer，观察者模式：定义对象间一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知自动更新。 
        八、Template Method，模板方法：定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，TemplateMethod使得子类可以不改变一个算法的结构即可以重定义该算法得某些特定步骤。 
        九、Command，命令模式：将一个请求封装为一个对象，从而使你可以用不同的请求对客户进行参数化，对请求排队和记录请求日志，以及支持可撤销的操作。 
        十、State，状态模式：允许对象在其内部状态改变时改变他的行为。对象看起来似乎改变了他的类。 
        十一、Strategy，策略模式：定义一系列的算法，把他们一个个封装起来，并使他们可以互相替换，本模式使得算法可以独立于使用它们的客户。 
        十二、China of Responsibility，职责链模式：使多个对象都有机会处理请求，从而避免请求的送发者和接收者之间的耦合关系 
        十三、Mediator，中介者模式：用一个中介对象封装一些列的对象交互。 
        十四、Visitor，访问者模式：表示一个作用于某对象结构中的各元素的操作，它使你可以在不改变各元素类的前提下定义作用于这个元素的新操作。 
        十五、Interpreter，解释器模式：给定一个语言，定义他的文法的一个表示，并定义一个解释器，这个解释器使用该表示来解释语言中的句子。 
        十六、Memento，备忘录模式：在不破坏对象的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。 
    结构型有： 
        十七、Composite，组合模式：将对象组合成树形结构以表示部分整体的关系，Composite使得用户对单个对象和组合对象的使用具有一致性。 
        十八、Facade，外观模式：为子系统中的一组接口提供一致的界面，fa?ade提供了一高层接口，这个接口使得子系统更容易使用。 
        十九、Proxy，代理模式：为其他对象提供一种代理以控制对这个对象的访问 
        二十、Adapter,适配器模式：将一类的接口转换成客户希望的另外一个接口，Adapter模式使得原本由于接口不兼容而不能一起工作那些类可以一起工作。 
        二十一、Decrator，装饰模式：动态地给一个对象增加一些额外的职责，就增加的功能来说，Decorator模式相比生成子类更加灵活。 
        二十二、Bridge，桥模式：将抽象部分与它的实现部分相分离，使他们可以独立的变化。 
        二十三、Flyweight，享元模式

##   12.AOP适合那些场景使用？
    
    Authentication 权限
    
    Caching 缓存
    
    Context passing 内容传递
    
    Error handling 错误处理
    
    Lazy loading 懒加载
    
    Debugging 调试
    
    logging, tracing, profiling and monitoring 记录跟踪 优化 校准
    
    Performance optimization 性能优化
    
    Persistence 持久化
    
    Resource pooling 资源池
    
    Synchronization 同步
    
    Transactions 事务
        