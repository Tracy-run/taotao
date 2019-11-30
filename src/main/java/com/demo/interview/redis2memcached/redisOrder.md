##REDIS command

Redis提供了丰富的命令对数据库和各种数据类型进行操作。 
    ###（1）keys 返回满足给定pattern的所有键值。例如返回所有的key用：keys

##Redis提供了丰富的命令对数据库和各种数据类型进行操作。

######（1）keys

    返回满足给定pattern的所有键值。例如返回所有的key用：keys *

###（2）exists

    确认一个key是否存在。

###（3）del

    删除一个键。例如：del name

###（4）expire

    设置一个键的过期时间。例如：expire age 10，10s后过期。

###（5）ttl

    查看还有多长时间过期。比如：ttl age，过期后返回-1。

###（6）move

    将当前数据库中的key转移到其它数据库中。当然在这之前，我们要先选择数据库，用select语句。
    
    比如select 0表示选择0数据库，在Redis中，一共有16个数据库，分别是0~15，一般情况下，进入数据
    
    库默认编号是0，如果我们要进入指定数据库，可以用select语句，select 1表示进入编号为1的数据库。
    
    现在，我们要把0号数据库中的age移动到1号数据库，，那么直接：move age 1

###（7）persist

    移除给定key的过期时间。例如：persist age

###（8）randomkey

    随机返回数据库中的一个键。例如：randomkey

###（9）rename

    重命名key。例如：rename age age1，表示把age重命名为age1

###（10）type

    返回键值的类型。比如：type age

###（11）echo

    打印一些内容。例如：echo name，就输出"name"。

###（12）quit

    退出连接。

###（13）dbsize

    返回当前数据库中所有key的数目。

###（14）info

    获取服务器的信息和统计。

###（15）config get

    实时传储收到的请求。例如：config get dir

###（16）flushdb

    删除当前数据库中的所有key。

###（17）flushall
    
    清空所有数据库中的所有key。
    
    Redis数据库的高级应用

###（1）为Redis数据库设置密码

    只需要在redis的配置文件中设置requirepass后面加上自己指定的密码就行了。启动的时候再指定配置
    
    文件。进入客户端后，我们发现能进入但是各种操作均被拒绝，所以我们还要进行授权，利用语句auth跟上
    
    密码即可。也可以登录客户端的时候输入密码，例如：redis-cli -a 88888888。

###（2）redis主从复制

redis主从复制的特点：

    1.一个master可以拥有多个slave
    
    2.多个slave可以连接同一个master外，还可以连接到其它slave
    
    3.主从复制不会阻塞master，在同步数据时，master可以继续处理client的请求
    
    4.提高系统的伸缩性

redis主从复制过程：

    1.slave与master建立连接，发送sync同步命令
    
    2.master会启动一个后台进程，将数据库快照保存到文件中，同时master主进程会开始收集新的写命令

    并缓存。

    3.后台完成保存后，就将此文件发送给slave
    
    4.slave将此文件保存到硬盘上

配置主从服务器：

配置slave服务器很简单，只需要在slave的配置文件中加入以下配置：

slaveof 222.27.174.98 6379 //指定master的主机的IP和端口号

masterauth 888888 //主机数据库的密码

我们可以通过info命令来查看本机的redis是主服务器还是从服务器。















































