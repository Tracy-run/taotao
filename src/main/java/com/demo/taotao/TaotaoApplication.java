package com.demo.taotao;

import com.github.pagehelper.PageHelper;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import javax.jms.Queue;

import java.util.Properties;

@SpringBootApplication
@EnableCaching
public class TaotaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaotaoApplication.class, args);
    }

    //配置mybatis的分页插件pageHelper
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","true");
        properties.setProperty("rowBoundsWithCount","true");
        properties.setProperty("reasonable","true");
        properties.setProperty("dialect","mysql");    //配置mysql数据库的方言
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    @Bean
    public Queue queue(){
        return new ActiveMQQueue("demo.queue");
    }
}
