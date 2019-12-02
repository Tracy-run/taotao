#RocketMQ介绍

RocketMQ是一款分布式消息中间件，最初是由阿里巴巴消息中间件团队研发并大规模应用于生产系统，
满足线上海量消息堆积的需求， 在2016年底捐赠给Apache开源基金会成为孵化项目，经过不到一年时间正式成为
了Apache顶级项目；早期阿里曾经基于ActiveMQ研发消息系统， 随着业务消息的规模增大，瓶颈逐渐显现，
后来也考虑过Kafka，**但因为在低延迟和高可靠性方面没有选择**，最后才自主研发了RocketMQ， 各方面的性能都比目前
已有的消息队列要好，RocketMQ和Kafka在概念和原理上都非常相似，所以也经常被拿来对比；
**RocketMQ默认采用长轮询的拉模式**， 单机支持千万级别的消息堆积，可以非常好的应用在海量消息系统中。
NameServer可以部署多个，相互之间独立，其他角色同时向多个NameServer机器上报状态信息，从而达到热备份的目的。
 NameServer本身是无状态的，也就是说NameServer中的Broker、Topic等状态信息不会持久存储，都是由各个角色定时
 上报并 存储到内存中的(NameServer支持配置参数的持久化，一般用不到)。
为何不用ZooKeeper？ZooKeeper的功能很强大，包括自动Master选举等，RocketMQ的架构设计决定了它不需要进行
Master选举， 用不到这些复杂的功能，只需要一个轻量级的元数据服务器就足够了。值得注意的是，
NameServer并没有提供类似Zookeeper的watcher机制， 而是采用了每30s心跳机制。

##RocketMQ 由哪些角色组成？

>生产者（Producer）：负责产生消息，生产者向消息服务器发送由业务应用程序系统生成的消息。

>消费者（Consumer）：负责消费消息，消费者从消息服务器拉取信息并将其输入用户应用程序。

>消息服务器（Broker）：是消息存储中心，主要作用是接收来自 Producer 的消息并存储，
 Consumer 从这里取得消息。

>名称服务器（NameServer）：用来保存 Broker 相关 Topic 等元信息并给 Producer ，提供 Consumer 查找
 Broker 信息。

##RocketMQ执行流程
>1、启动 Namesrv，Namesrv起 来后监听端口，等待 Broker、Producer、Consumer 连上来，相当于一个路由控制中心。

>2、Broker 启动，跟所有的 Namesrv 保持长连接，定时发送心跳包。

>3、收发消息前，先创建 Topic 。创建 Topic 时，需要指定该 Topic 要存储在 哪些 Broker上。
也可以在发送消息时自动创建Topic。

>4、Producer 发送消息。

>5、Consumer 消费消息。

##请说说你对 Producer 的了解？

>1、获得 Topic-Broker 的映射关系。
    Producer 启动时，也需要指定 Namesrv 的地址，从 Namesrv 集群中选一台建立长连接。
    生产者每 30 秒从 Namesrv 获取 Topic 跟 Broker 的映射关系，更新到本地内存中。
    然后再跟 Topic 涉及的所有 Broker 建立长连接，每隔 30 秒发一次心跳。

>2、生产者端的负载均衡。
    生产者发送时，会自动轮询当前所有可发送的broker，一条消息发送成功，下次换另外一个broker发送，
    以达到消息平均落到所有的broker上。

##说说你对 Consumer 的了解？

>1、获得 Topic-Broker 的映射关系。
    Consumer 启动时需要指定 Namesrv 地址，与其中一个 Namesrv 建立长连接。消费者每隔 30 秒从 Namesrv 
    获取所有Topic 的最新队列情况，
    Consumer 跟 Broker 是长连接，会每隔 30 秒发心跳信息到Broker .

>2、消费者端的负载均衡。根据消费者的消费模式不同，负载均衡方式也不同。

##消费者消费模式有几种？

消费者消费模式有两种：**集群消费和广播消费。**

>集群消费
消费者的一种消费模式。一个 Consumer Group 中的各个 Consumer 实例分摊去消费消息，
即一条消息只会投递到一个 Consumer Group 下面的一个实例。

>广播消费
消费者的一种消费模式。消息将对一 个Consumer Group 下的各个 Consumer 实例都投递一遍。
即即使这些 Consumer 属于同一个Consumer Group ，消息也会被 Consumer Group 中的每个 Consumer 都消费一次。

##消费者获取消息有几种模式？

消费者获取消息有两种模式：**推送模式和拉取模式**。

>PushConsumer
推送模式（虽然 RocketMQ 使用的是长轮询）的消费者。消息的能及时被消费。使用非常简单，
内部已处理如线程池消费、流控、负载均衡、异常处理等等的各种场景。
长轮询，就是我们在 《 精尽【消息队列 】面试题》 提到的，push + pull 模式结合的方式。

>PullConsumer
拉取模式的消费者。应用主动控制拉取的时机，怎么拉取，怎么消费等。主动权更高。但要自己处理各种场景。

##什么是定时消息？如何实现？
定时消息，是指消息发到 Broker 后，不能立刻被 Consumer 消费，要到特定的时间点或者等待特定的时间后才能被消费。

##mq消息的丢失、队列的使用策略，为什么要用mq
消息重复：造成消息的重复的根本原因是：网络不可达。只要通过网络交换数据，就无法避免这个问题。
保证每条消息都有唯一编号且保证消息处理成功与去重表的日志同时出现

##心跳机制
    单个Broker跟所有Namesrv保持心跳请求，心跳间隔为30秒，心跳请求中包括当前Broker所有的Topic信息。
    Namesrv会反查Broer的心跳信息， 如果某个Broker在2分钟之内都没有心跳，则认为该Broker下线，
    调整Topic跟Broker的对应关系。但此时Namesrv不会主动通知Producer、Consumer有Broker宕机。

    Consumer跟Broker是长连接，会每隔30秒发心跳信息到Broker。Broker端每10秒检查一次当前存活的Consumer，
    若发现某个Consumer 2分钟内没有心跳， 就断开与该Consumer的连接，并且向该消费组的其他实例发送通知，
    触发该消费者集群的负载均衡(rebalance)。
    生产者每30秒从Namesrv获取Topic跟Broker的映射关系，更新到本地内存中。再跟Topic涉及的所有Broker建立长连接，
    每隔30秒发一次心跳。 在Broker端也会每10秒扫描一次当前注册的Producer，如果发现某个Producer超过2分钟都
    没有发心跳，则断开连接。

    Namesrv压力不会太大，平时主要开销是在维持心跳和提供Topic-Broker的关系数据。但有一点需要注意，
    Broker向Namesrv发心跳时， 会带上当前自己所负责的所有Topic信息，如果Topic个数太多（万级别），
    会导致一次心跳中，就Topic的数据就几十M，网络情况差的话， 网络传输失败，心跳失败，
    导致Namesrv误认为Broker心跳失败。
    
    每个主题可设置队列个数，自动创建主题时默认4个，需要顺序消费的消息发往同一队列，
    比如同一订单号相关的几条需要顺序消费的消息发往同一队列， 顺序消费的特点的是，
    不会有两个消费者共同消费任一队列，且当消费者数量小于队列数时，消费者会消费多个队列。
    至于消息重复，在消 费端处理。RocketMQ 4.3+支持事务消息，可用于分布式事务场景(最终一致性)。
    
    Broker上存Topic信息，Topic由多个队列组成，队列会平均分散在多个Broker上。
    Producer的发送机制保证消息尽量平均分布到 所有队列中，最终效果就是所有消息都平均落在每个Broker上。

##RocketMQ的消息的存储是由ConsumeQueue和CommitLog配合来完成的

    ConsumeQueue中只存储很少的数据，消息主体都是通过CommitLog来进行读写。 
    如果某个消息只在CommitLog中有数据，而ConsumeQueue中没有，则消费者无法消费，
    RocketMQ的事务消息实现就利用了这一点。
    
    CommitLog：是消息主体以及元数据的存储主体，对CommitLog建立一个ConsumeQueue，
    每个ConsumeQueue对应一个（概念模型中的）MessageQueue，所以只要有 CommitLog在，
    ConsumeQueue即使数据丢失，仍然可以恢复出来。
    
    ConsumeQueue：是一个消息的逻辑队列，存储了这个Queue在CommitLog中的起始offset，
    log大小和MessageTag的hashCode。每个Topic下的每个Queue都有一个对应的 ConsumeQueue文件，
    例如Topic中有三个队列，每个队列中的消息索引都会有一个编号，编号从0开始，往上递增。
    并由此一个位点offset的概念，有了这个概念，就可以对 Consumer端的消费情况进行队列定义。
    
    RocketMQ的高性能在于顺序写盘(CommitLog)、零拷贝和跳跃读(尽量命中PageCache)，
    高可靠性在于刷盘和Master/Slave，另外NameServer 全部挂掉不影响已经运行的Broker,Producer,Consumer。
    
    发送消息负载均衡，且发送消息线程安全(可满足多个实例死循环发消息)，集群消费模式下消费者端负载均衡，
    这些特性加上上述的高性能读写， 共同造就了RocketMQ的高并发读写能力。
    
    刷盘和主从同步均为异步(默认)时，broker进程挂掉(例如重启)，消息依然不会丢失，
    因为broker shutdown时会执行persist。 当物理机器宕机时，才有消息丢失的风险。另外，master挂掉后，
    消费者从slave消费消息，但slave不能写消息。

##RocketMQ具有很好动态伸缩能力(非顺序消息)，伸缩性体现在Topic和Broker两个维度。

Topic维度：假如一个Topic的消息量特别大，但集群水位压力还是很低，就可以扩大该Topic的队列数，
Topic的队列数跟发送、消费速度成正比。
Broker维度：如果集群水位很高了，需要扩容，直接加机器部署Broker就可以。Broker起来后向Namesrv注册，
Producer、Consumer通过Namesrv 发现新Broker，立即跟该Broker直连，收发消息。

Producer: 失败默认重试2次；sync/async；ProducerGroup，在事务消息机制中，如果发送消息的producer
在还未commit/rollback前挂掉了，broker会在一段时间后回查ProducerGroup里的其他实例，
确认消息应该commit/rollback

Consumer: DefaultPushConsumer/DefaultPullConsumer，push也是用pull实现的，采用的是长轮询方式；
CLUSTERING模式下，一条消息只会被ConsumerGroup里的一个实例消费，但可以被多个不同的ConsumerGroup消费，
BROADCASTING模式下，一条消息会被ConsumerGroup里的所有实例消费。

DefaultPushConsumer: Broker收到新消息请求后，如果队列里没有新消息，并不急于返回，
通过一个循环不断查看状态，每次waitForRunning一段时间(5s)，然后在check。当一直没有新消息，
第三次check时，等待时间超过suspendMaxTimeMills(15s)，就返回空结果。在等待的过程中，
Broker收到了新的消息后会直接调用notifyMessageArriving返回请求结果。“长轮询”的核心是，
Broker端Hold住(挂起)客户端客户端过来的请求一小段时间，在这个时间内有新消息到达，
就利用现有的连接立刻返回消息给Consumer。“长轮询”的主动权还是掌握在Consumer手中，
Broker即使有大量消息积压，也不会主动推送给Consumer。长轮询方式的局限性，
是在Hold住Consumer请求的时候需要占用资源，它适合用在消息队列这种客户端连接数可控的场景中。

DefaultPullConsumer: 需要用户自己处理遍历MessageQueue、保存Offset，所以PullConsumer有更多的自主性和灵活性。
对于集群模式的非顺序消息，消费失败默认重试16次，延迟等级为3~18。(messageDelayLevel = 
“1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h”)
