package com.demo.taotao.service;

import com.demo.taotao.pojo.User;

import java.util.List;

public interface UserService {

    List<User>  findAll();

    List<User> queryUserByName(String name);

//    List<User> queryUserByPage(Integer page,Integer size);

    List<User> findItemByPage(Integer page,Integer size);
}
