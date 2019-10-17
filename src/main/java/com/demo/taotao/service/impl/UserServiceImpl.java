package com.demo.taotao.service.impl;

import com.demo.taotao.common.pageutil.PageBean;
import com.demo.taotao.dao.UserDao;
import com.demo.taotao.dao.UserMapper;
import com.demo.taotao.pojo.User;
import com.demo.taotao.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userdao;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userdao.findAll();
    }


    public List<User> queryAll() {
        return this.userMapper.queryAll();
    }


    @Override
    public List<User> queryUserByName(String name){
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
