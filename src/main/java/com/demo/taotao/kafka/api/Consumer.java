package com.demo.taotao.kafka.api;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import scala.collection.immutable.HashMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class Consumer {


    /**
     * enable.auto.commit：是否开启自动提交offset功能
     *
     * auto.commit.interval.ms：自动提交offset的时间间隔
     *
     * @param args
     */
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers","hadoop102:9092");
        props.put("group.id","test");
        //自动提交
        props.put("enable.auto.commit","true");
        props.put("auto.commit.interval.ms","1000");
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList("first"));

        while (true){
            ConsumerRecords<String,String> records = consumer.poll(100);

            for(ConsumerRecord<String,String> record:records)
                System.out.printf("offset = %d, key = %s, value = %s%n" + record.offset(), record.key(), record.value());
        }

    }

    /**
     *
     *虽然自动提交offset十分简介便利，但由于其是基于时间提交的，开发人员难以把握offset提交的时机。
     * 因此Kafka还提供了手动提交offset的API
     *
     *
     * 手动提交offset的方法有两种：分别是commitSync（同步提交）和commitAsync（异步提交）。
     * 两者的相同点是，都会将本次poll的一批数据最高的偏移量提交；
     * 不同点是，commitSync阻塞当前线程，一直到提交成功，并且会自动失败重试（由不可控因素导致，也会出现提交失败）；
     * 而commitAsync则没有失败重试机制，故有可能提交失败。
     *
     *
     */


    /**
     * 同步提交，当前线程会阻塞知道offset提交成功
     * @param args
     */
    public static void commitSync(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers","hadoop102:9092");
        props.put("group.id","test");//消费者组，只要group.id相同，就属于同一个消费者组
        props.put("enable.auto.commit","false");//关闭自动提交offset
        props.put("key.serialization","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.serialization","org.apache.kafka.common.serialization.StringDeserializer");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList("first"));

        while (true){
            ConsumerRecords<String,String> records = consumer.poll(100);//消费者拉取数据

            for(ConsumerRecord record:records){
                System.out.printf("offset = %d, key = %s, value = %s%n",record.offset(),record.key(),record.value());
            }

            consumer.commitSync();//同步提交，当前线程会阻塞知道offset提交成功

        }

    }


    /**
     * 异步提交
     * @param args
     */
    public static void commitAsync(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop102:9092");//Kafka集群
        props.put("group.id", "test");//消费者组，只要group.id相同，就属于同一个消费者组
        props.put("enable.auto.commit", "false");//关闭自动提交offset
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList("first"));//消费者订阅主题

        while (true) {

            ConsumerRecords<String, String> records = consumer.poll(100);//消费者拉取数据

            for(ConsumerRecord<String,String> record:records){

                System.out.printf("offset = %d, key = %s, value = %s%n",record.offset(),record.key(),record.value());

                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception e) {
                        if(null == e){
                            System.err.println("commit failed for " + offsets);
                        }
                    }
                });//异步提交

            }

        }

    }


    /**
     * Kafka 0.9版本之前，offset存储在zookeeper，0.9版本之后，默认将offset存储在Kafka的一个内置的topic中。
     * 除此之外，Kafka还可以选择自定义存储offset。
     *
     * Offset的维护是相当繁琐的，因为需要考虑到消费者的Rebalace。
     *
     * 当有新的消费者加入消费者组、已有的消费者退出消费者组或者所订阅的主题的分区发生变化，就会触发到分区的重新分配，重新分配的过程叫做Rebalance。
     *
     * 消费者发生Rebalance之后，每个消费者消费的分区就会发生变化。因此消费者要首先获取到自己被重新分配到的分区，
     * 并且定位到每个分区最近提交的offset位置继续消费。
     *
     * 要实现自定义存储offset，需要借助ConsumerRebalanceListener，以下为示例代码，其中提交和获取offset的方法，
     * 需要根据所选的offset存储系统自行实现
     *
     */
    private static Map<TopicPartition, Long> currentOffset = new HashMap<>();

    public static void customConsumerOffset (String[] args) {

        Properties props = new Properties();

        props.put("bootstrap.servers", "hadoop102:9092");//Kafka集
        props.put("group.id", "test");//消费者组，只要group.id相同，就属于同一个消费者组
        props.put("enable.auto.commit", "false");//关闭自动提交offset
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList("first"), new ConsumerRebalanceListener() {
            //该方法会在Rebalance之前调用
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                commitOffset(currentOffset);
            }

            //该方法会在Rebalance之后调用
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                currentOffset.clear();
                for(TopicPartition pp:partitions){
                    consumer.seek(pp,getOffset(pp));
                }
            }
        });

    }
    //获取某分区的最新offset
    private static long getOffset(TopicPartition partition) {
        return 0;
    }
    //提交该消费者所有分区的offset
    private static void commitOffset(Map<TopicPartition, Long> currentOffset) {
    }




}
