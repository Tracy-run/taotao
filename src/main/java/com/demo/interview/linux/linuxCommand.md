# linux常规操作

>https://blog.csdn.net/xulong_08/article/details/81463054
 Linux是目前应用最广泛的服务器操作系统，基于Unix，开源免费，由于系统的稳定性和安全性，市场占有率很高，几乎成为程序代码运行的最佳系统环境。
 linux不仅可以长时间的运行我们编写的程序代码，还可以安装在各种计算机硬件设备中，如手机、路由器等，Android程序最底层就是运行在linux系统上的。

## 一、linux的目录结构
 
    [640.jps]
    
    / 下级目录结构
    bin (binaries)存放二进制可执行文件
    sbin (super user binaries)存放二进制可执行文件，只有root才能访问
    etc (etcetera)存放系统配置文件
    usr (unix shared resources)用于存放共享的系统资源
    home 存放用户文件的根目录
    root 超级用户目录
    dev (devices)用于存放设备文件
    lib (library)存放跟文件系统中的程序运行所需要的共享库及内核模块
    mnt (mount)系统管理员安装临时文件系统的安装点
    boot 存放用于系统引导时使用的各种文件
    tmp (temporary)用于存放各种临时文件
    var (variable)用于存放运行时需要改变数据的文件

## 二、linux常用命令
    
    命令格式：命令 -选项 参数 （选项和参数可以为空）
    如：ls-la/usr

   命令 | 参数 | 示例 | 说明






## 三、JVM 定位问题工具

    在 JDK 安装目录的 bin 目录下默认提供了很多有价值的命令行工具。每个小工具体积基本都比较小，因为这些工具只是 jdk\lib\tools.jar 的简单封装。
    
    其中，定位排查问题时最为常用命令包括:jps（进程）、jmap（内存）、jstack（线程）、jinfo(参数) 等。
    
    jps: 查询当前机器所有 JAVA 进程信息；
    
    jmap: 输出某个 java 进程内存情况 (如:产生那些对象及数量等)；
    
    jstack: 打印某个 Java 线程的线程栈信息；
    
    jinfo: 用于查看 jvm 的配置参数。
















