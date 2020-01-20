package com.demo.interview.mysql;


/**
 *
 * 主要区别：
 *
 * 1、MyIASM是非事务安全的，而InnoDB是事务安全的
 *
 * 2、MyIASM锁的粒度是表级的，而InnoDB支持行级锁
 *
 * 3、MyIASM支持全文类型索引，而InnoDB不支持全文索引
 *
 * 4、MyIASM相对简单，效率上要优于InnoDB，小型应用可以考虑使用MyIASM
 *
 * 5、MyIASM表保存成文件形式，跨平台使用更加方便
 *
 *
 *应用场景：
 * 1、MyIASM管理非事务表，提供高速存储和检索以及全文搜索能力，如果再应用中执行大量select操作，应该选择MyIASM
 * 2、InnoDB用于事务处理，具有ACID事务支持等特性，如果在应用中执行大量insert和update操作，应该选择InnoDB
 *
 */
public class Diff2Driver {



}
