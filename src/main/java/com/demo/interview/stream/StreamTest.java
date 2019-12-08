package com.demo.interview.stream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.Arrays;
import java.util.Properties;

public class StreamTest {

    // 测试输入消息
    private static final List<ActingOuterClass.Acting> TEST_ACTING_EVENTS = Arrays.asList(
            ActingOuterClass.Acting.newBuilder().setName("Meryl Streep").setTitle("The Iron Lady").setGenre("drama").build(),
            ActingOuterClass.Acting.newBuilder().setName("Will Smith").setTitle("Men in Black").setGenre("comedy").build(),
            ActingOuterClass.Acting.newBuilder().setName("Matt Damon").setTitle("The Martian").setGenre("drama").build(),
            ActingOuterClass.Acting.newBuilder().setName("Judy Garlandp").setTitle("The Wizard of Oz").setGenre("fantasy").build(),
            ActingOuterClass.Acting.newBuilder().setName("Jennifer Aniston").setTitle("Office Space").setGenre("comedy").build(),
            ActingOuterClass.Acting.newBuilder().setName("Bill Murray").setTitle("Ghostbusters").setGenre("fantasy").build(),
            ActingOuterClass.Acting.newBuilder().setName("Christian Bale").setTitle("The Dark Knight").setGenre("crime").build(),
            ActingOuterClass.Acting.newBuilder().setName("Laura Dern").setTitle("Jurassic Park").setGenre("fantasy").build(),
            ActingOuterClass.Acting.newBuilder().setName("Keanu Reeves").setTitle("The Matrix").setGenre("fantasy").build(),
            ActingOuterClass.Acting.newBuilder().setName("Russell Crowe").setTitle("Gladiator").setGenre("drama").build(),
            ActingOuterClass.Acting.newBuilder().setName("Diane Keaton").setTitle("The Godfather: Part II").setGenre("crime").build()
    );

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, new ProtobufSerializer<ActingOuterClass.Acting>().getClass());

        try (final Producer<String, ActingOuterClass.Acting> producer = new KafkaProducer(props)) {
            TEST_ACTING_EVENTS.stream().map(acting -> new ProducerRecord<String, ActingOuterClass.Acting>("acting-events", acting))
                    .forEach(producer::send);
        }

    }

    private Topology buildTopology(Properties envProps) {
        final StreamsBuilder builder = new StreamsBuilder();
        final String inputTopic = envProps.getProperty("input.topic.name");

        KStream<String, ActingOuterClass.Acting>[] branches = builder
                .stream(inputTopic, Consumed.with(Serdes.String(), actingProtobufSerdes()))
                .branch((key, value) -> "drama".equalsIgnoreCase(value.getGenre()),
                        (key, value) -> "fantasy".equalsIgnoreCase(value.getGenre()),
                        (key, value) -> true);
        branches[0].to(envProps.getProperty("output.drama.topic.name"));
        branches[1].to(envProps.getProperty("output.fantasy.topic.name"));
        branches[2].to(envProps.getProperty("output.other.topic.name"));

        return builder.build();
    }
}


class ActingOuterClass{
   public String Acting;
}





