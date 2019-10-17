package com.demo.taotao.dao;

import com.demo.taotao.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface UserMapper {


    @Select("select * from user where name like '%${value}%' ")
    List<User> queryUserByName(String name);

    @Select("select count(1) from user")
    int countItem();

    List<User> queryAll();

    List<User> queryAll(String name);
}
