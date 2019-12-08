##Java架构-KafKa集群安装详细步骤
    https://blog.csdn.net/coco_wditm/article/details/84139700


###1.在根目录创建kafka文件夹（service1、service2、service3都创建）

[root@localhost /]# mkdir kafka

###2.通过Xshell上传文件到service1服务器：上传kafka_2.9.2-0.8.1.1.tgz到/software文件夹

###3.远程copy将service1下的/software/kafka_2.9.2-0.8.1.1.tgz到service2、service3

[root@localhost software]# scp -r /software/kafka_2.9.2-0.8.1.1.tgz root@192.168.2.212:/software/

[root@localhost software]# scp -r /software/kafka_2.9.2-0.8.1.1.tgz root@192.168.2.213:/software/

###3.copy /software/kafka_2.9.2-0.8.1.1.tgz到/kafka/目录（service1、service2、service3都执行）

[root@localhost software]# cp /software/kafka_2.9.2-0.8.1.1.tgz /kafka/

###4.安装解压kafka_2.9.2-0.8.1.1.tgz（service1、service2、service3都执行）

[root@localhost /]# cd /kafka/

[root@localhost kafka]# tar -zxvf kafka_2.9.2-0.8.1.1.tgz

###5.创建kafka消息目录（service1,service2,service3都要创建）

[root@localhost kafka]# mkdir kafkaLogs

###6. 修改kafka的配置文件（service1,service2,service3都要配置）

[root@localhost /]# cd /kafka/kafka_2.9.2-0.8.1.1/

[root@localhost kafka_2.9.2-0.8.1.1]# cd config/

[root@localhost config]# ls

consumer.properties log4j.properties producer.properties server.properties test-log4j.properties tools-log4j.properties zookeeper.properties

[root@localhost config]# vi server.properties

    具体配置文件信息查看官方配置详情







###7.启动kafka服务

[root@localhost bin]# ./kafka-server-start.sh -daemon …/config/server.properties

[root@localhost bin]# jps

27413 Kafka

27450 Jps

17884 QuorumPeerMain

###8.验证kafka集群

[root@localhost bin]# ./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 2 --partitions 1 --topic test

Created topic “test”.

###9.在service1上开启producer程序

./kafka-console-producer.sh --broker-list 192.168.2.211:9092 --topic test

[root@localhost bin]# ./kafka-console-producer.sh --broker-list 192.168.2.211:9092 --topic test

SLF4J: Failed to load class “org.slf4j.impl.StaticLoggerBinder”.

SLF4J: Defaulting to no-operation (NOP) logger implementation

SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

###10. 在service2上开启consumer程序

[root@localhost bin]# ./kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning

SLF4J: Failed to load class “org.slf4j.impl.StaticLoggerBinder”.

SLF4J: Defaulting to no-operation (NOP) logger implementation

SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

###11.在producer中发送消息：hello honghu

[root@localhost bin]# ./kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning

SLF4J: Failed to load class “org.slf4j.impl.StaticLoggerBinder”.

SLF4J: Defaulting to no-operation (NOP) logger implementation

SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

hello honghu

###12. 在consumer中接受到消息

[root@localhost bin]# ./kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning

SLF4J: Failed to load class “org.slf4j.impl.StaticLoggerBinder”.

SLF4J: Defaulting to no-operation (NOP) logger implementation

SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

hello honghu








































































