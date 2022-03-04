package com.example.toyuserservice.config;

import com.example.toyuserservice.constants.KafkaConstants.CONSUMER_GROUP;
import com.example.toyuserservice.constants.KafkaConstants.Topic;
import com.example.toyuserservice.kafka_listener.DefaultMongoUpdateDeserializer;
import com.example.toyuserservice.kafka_listener.KafkaTestDto;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public ObjectMapper kafkaObjectMapper(){
        ObjectMapper objectMapper = JacksonUtils.enhancedObjectMapper();
        SimpleModule module = new SimpleModule("DefaultMongoUpdateDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(KafkaTestDto.class, new DefaultMongoUpdateDeserializer(KafkaTestDto.class));
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        return containerFactory;
    }
//
    @Bean
    public ConsumerFactory<String, Object> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public Map<String, Object> consumerConfig(){
        HashMap<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP.USER_SERVICE);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return configMap;
    }

    // Producer(Publisher) 설정
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public Map<String, Object> producerConfig(){
        HashMap<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }

    @Bean
    public NewTopic userUpdate(){
        return TopicBuilder
                .name(Topic.UPDATE_USER)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic userUpdate2(){
        return TopicBuilder
                .name(Topic.UPDATE_USER2)
                .partitions(3)
                .replicas(3)
                .build();
    }


}
