#memcached


## Memcached的内存管理机制
>   Memcached默认使用Slab Allocation机制管理内存，其主要思想是按照预先规定的大小，将分配的内存分割成特定长度的块以存储相应
    长度的key-value数据记录，
    以完全解决内存碎片问题。Slab Allocation机制只为存储外部数据而设计，也就是说所有的key-value数据都存储在Slab Allocation系统里，
    而Memcached的其它内存请求则通过普通的malloc/free来申请，因为这些请求的数量和频率决定了它们不会对整个系统的性能造成影响

>   Slab Allocation的原理相当简单。如图3所示，它首先从操作系统申请一大块内存，并将其分割成各种尺寸的块Chunk，
    并把尺寸相同的块分成组Slab Class。
    其中，Chunk就是用来存储key-value数据的最小单位。每个Slab Class的大小，可以在Memcached启动的时候通过制定Growth Factor来控制。
    假定Figure 1中Growth Factor的取值为1.25，所以如果第一组Chunk的大小为88个字节，第二组Chunk的大小就为112个字节，依此类推。
    
## Memcached内存管理架构    
    
>  当Memcached接收到客户端发送过来的数据时首先会根据收到数据的大小选择一个最合适的Slab Class，然后通过查询Memcached
  保存着的该Slab Class内空闲Chunk的列表就可以找到一个可用于存储数据的Chunk。当一条数据库过期或者丢弃时，
  该记录所占用的Chunk就可以回收，重新添加到空闲列表中。 从以上过程我们可以看出
  Memcached的内存管理制效率高，而且不会造成内存碎片，但是它最大的缺点就是会导致空间浪费。因为每个 Chunk都分配了特定长度的内存空间，
  所以变长数据无法充分利用这些空间。如图 4所示，将100个字节的数据缓存到128个字节的Chunk中，剩余的28个字节就浪费掉了。  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    