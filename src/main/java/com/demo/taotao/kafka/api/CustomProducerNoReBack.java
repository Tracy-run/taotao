package com.demo.taotao.kafka.api;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 不带回调函数的API
 */
public class CustomProducerNoReBack {


    public static void main(String[] args) throws ExecutionException {

        Properties props = new Properties();
        props.put("bootstrap.servers","hadoop102:9092");//kafka集群，broker-list
        props.put("acks","all");
        props.put("retries",1);//重试次数
        props.put("batch.size",16384);//批次大小
        props.put("linger.ms",1);//等待时间
        props.put("buffer.memory",33554432);//RecordAccumulator缓冲区大小
        props.put("key.serialization","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serialization","org.apache.kafka.common.serialization.StringSerializer");


        Producer<String,String> producer = new KafkaProducer<String, String>(props);
        for(int i=0;i<100 ;i++){
            //每条数据都要封装成一个ProducerRecord对象
            producer.send(new ProducerRecord<>("first",Integer.toString(i),Integer.toString(i)));
        }
        producer.close();

    }


}
