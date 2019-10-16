package com.demo.taotao.dao;

import com.demo.taotao.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {


    @Select("select * from user where name like '%${value}%' ")
    List<User> queryUserByName(String name);

}
