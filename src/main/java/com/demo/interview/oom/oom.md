#oom问题定位

> -XX:+PrintGCDetails -Xloggc:/data/logs/JavaDemo/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/JavaDemo -Xms1m -Xmx1m、
 其中：
 -XX:+PrintGCDetails 打印gc日志
 -Xloggc:/data/logs/JavaDemo/gc.log gc日志的地址
 -XX:+HeapDumpOnOutOfMemoryError 出现内存溢出异常时dump出当前的内存转储快照
 -XX:HeapDumpPath=/data/logs/JavaDemo 指定快照的存放位置
 -Xms1m jvm初始内存
 -Xmx1m jvm最大内存
 此处设置jvm内存较小是为了之后模拟oom时更快报错。平时项目里不用这么设置。















































