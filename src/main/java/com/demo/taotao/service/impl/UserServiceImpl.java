package com.demo.taotao.service.impl;

import com.demo.taotao.common.pageutil.PageBean;
import com.demo.taotao.dao.UserDao;
import com.demo.taotao.dao.UserMapper;
import com.demo.taotao.pojo.User;
import com.demo.taotao.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userdao;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Cacheable(value = "userCache",key = "user.findAll")
    @Override
    public List<User> findAll() {
        System.out.println("从Mysql中查询");
        return userdao.findAll();
    }


    public List<User> queryAll() {
        return this.userMapper.queryAll();
    }


    @CacheEvict(value = "userCache",key = "user.findAll")
    @Override
    public List<User> queryUserByName(String name){

        //****************************redis的深入使用************************************
        //保存数据
        this.redisTemplate.boundValueOps("redis").set("hello redis");
        //有效期为100s
        this.redisTemplate.boundValueOps("redis").expire(100l, TimeUnit.SECONDS);
        // 给value每次执行加一操作
        this.redisTemplate.boundValueOps("count").increment(1l);
        //****************************redis的深入使用************************************

        System.out.println("缓存清理了！");
        List<User> list = userMapper.queryUserByName(name);
        return list;
    }

  /*  @Override
    public List<User> queryUserByPage(Integer page, Integer size) {

        //set startPage and  pageSize
        PageHelper.startPage(page,size);

        //使用mapper查询
        List<User> list = this.userMapper.queryAll(null);
        return list;
    }*/

    @Override
    public List<User> findItemByPage(Integer page, Integer size) {
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(page, size);

        List<User> allItems = userMapper.queryAll();        //全部商品
        int countNums = userMapper.countItem();            //总记录数
        PageBean<User> pageData = new PageBean<>(page, size, countNums);
        pageData.setItems(allItems);
        return pageData.getItems();
    }
}
