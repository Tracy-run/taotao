package com.demo.taotao.controller;


import com.demo.taotao.pojo.User;
import com.demo.taotao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/list")
    public List<User> queryUserAll(){
        List<User> list = this.userService.findAll();
        return list;
    }

    @RequestMapping("/list/{name}")
    public List<User> queryUserByName(@PathVariable String name){
        return this.userService.queryUserByName(name);
    }
}
