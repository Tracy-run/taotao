## 阿里一面

##   1.创建对象的方式
    
###     
    一般共2种
    1.new 类名
    2.反射
    3.使用Constructor类的newInstance方法
    4.clone


##   2.session和cookie的区别

###
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
##   7.不同服务间的调用协议 
##   8.linux中的指令  查询缓存  定位  查看进程 
##   9.MQ的消费者准确读取生产者值的原理
##   10.线程的原理
##   11.设计模式有哪些？
##   12.AOP适合那些场景使用？
