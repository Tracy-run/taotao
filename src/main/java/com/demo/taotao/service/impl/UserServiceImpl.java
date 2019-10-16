package com.demo.taotao.service.impl;

import com.demo.taotao.dao.UserDao;
import com.demo.taotao.dao.UserMapper;
import com.demo.taotao.pojo.User;
import com.demo.taotao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService,UserMapper{

    @Autowired
    UserDao userdao;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> findAll() {
        List<User> userlist = userdao.findAll();
        return userlist;
    }


    @Override
    public List<User> queryUserByName(String name){
        List<User> list = userMapper.queryUserByName(name);
        return list;
    }
}
