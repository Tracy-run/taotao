#DRIVER

##主要区别：
    1、MyIASM是非事务安全的，而InnoDB是事务安全的
    
    2、MyIASM锁的粒度是表级的，而InnoDB支持行级锁
    
    3、MyIASM支持全文类型索引，而InnoDB不支持全文索引
    
    4、MyIASM相对简单，效率上要优于InnoDB，小型应用可以考虑使用MyIASM
    
    5、MyIASM表保存成文件形式，跨平台使用更加方便

##应用场景：
    1、MyIASM管理非事务表，提供高速存储和检索以及全文搜索能力，如果再应用中执行大量select操作，应该选择MyIASM
    2、InnoDB用于事务处理，具有ACID事务支持等特性，如果在应用中执行大量insert和update操作，应该选择InnoDB

    
innodb: 事务优先  行级锁(适合高并发)  
myisam: 性能优先  表级锁
    
mysql的四个逻辑分层： 连接+服务+引擎+存储    
    
[https://edu.csdn.net/course/play/25283/297138]
##mySqlCommand:
  
    验证：mysqladmin --version
    启动mysql： service mysql start
    重启：service mysql restart
    
    手动启动服务： /etc/init.d/mysql start
    开机自启动： chkconfig mysql on  ,chkconfig mysql off
    检查开机是否自启动：ntsysv
    
    给用户root增加密码：/usr/bin/mysqladmin -u root password root
    登录: mysql -u root -p
    数据库存放目录：ps -ef | grep mysql
    
    
    mysql的配置文件路径 /usr/share/mysql
            my-huge.cnf     高端服务器  一般1-2G内存
            hy-large.cnf    中等规模
            my-medium.cnf   一般
            my-small.cnf    较小
            5.5版本：mysql默认的配置文件只能识别/etc/my.cnf  以上文件修改后需要覆盖/etc/my.cnf才生效
            5.6版本：默认配置文件/etc/mysql-default.cnf
    
    mysql字符集编码：
            sql: show variables like '%char%'
        
    mysql的逻辑分层： 
            连接层
            服务层
            引擎层(innodb[默认],myisam)
            存储层  
            show engines;  
    当前mysql默认引擎: show variables like '%engine%';
    
###二叉树：
    索引：相当于书的目录
        索引：index是帮助MYSQL高效的获取
        
        
        弊端：    
            哪些不适合做索引：
                1.本身数据量不是很大
                2.数据值变来变去修改  少量数据，频繁修改 很少使用的字段
                3.降低增删改的效率（增删改  查）
        优势：
            1.提高查询效率（降低IO使用率）
            2.降低CPU使用率（...order by age desc  B树索引本身是排好序的索引，排好序可直接使用）
    索引分类：
        单值索引： 单列，一个表可以多个单值索引
            create index colum_index on table(colum) 
            alter table  tablename add index colum_index(colum) 
        唯一索引： 唯一索引不能重复，数值唯一
            create unique index name_index on table(colum) 
            alter table tablename unique index colum_index(colum)
        符合索引： 多个列组成的索引
            create index colum_colum_index on table(colum,colum) 
            alter table tablename add index colum_colum_index(colum,colum)
     
        主键索引：索引值不能为null，唯一索引可以为null
        
        删除索引：
            drop index indexname on tablename
        
        查询索引：
            show index from tablename            
    
    
    
    
### B树 3层Btree可以存放百万条数据
    
    Btree一般指B+树，数据全部存放在叶节点中
    
    B+树查询任意的数据次数：n次
    
    
###sql性能问题
    a.分析sql的执行计划  
        explain： 可以模拟sql优化器质性SQL语句，查看sql的执行状况
    b.mysql查询优化器会干扰我们的优化    
    
    
    
    
    
    