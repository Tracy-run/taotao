package com.demo.taotao.jdbcTemplate.controller;

import com.demo.taotao.aqs.current.MyLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    static MyLock mylock = new MyLock();

    public String doJob(){

        mylock.lock();
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select stock from shop_order where id = 10");

        int stock;
        if(result == null || result.isEmpty() || (stock = (Integer)result.get(0).get("stock")) <= 0){
            logger.info("下单失败");
            return "下单失败，已经没有库存了";
        }

        stock--;
        jdbcTemplate.update("update shop_order set stock= ? where id = 10 ",stock);
        logger.info("下单成功，当前产品剩余数据为-->" + stock);

        mylock.unlock();

        return "下单成功，当前产品剩余数据为:  " + stock;
    }

}
