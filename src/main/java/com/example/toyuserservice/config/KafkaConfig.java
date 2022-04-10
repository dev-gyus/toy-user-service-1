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
        module.addDeserializer(KafkaTestDto.class, new DefaultMongoUpdateDeserializer<>(KafkaTestDto.class));
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaTestDto> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, KafkaTestDto> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        return containerFactory;
    }

    @Bean
    public ConsumerFactory<String, KafkaTestDto> consumerFactory(){
        JsonDeserializer<KafkaTestDto> jsonDeserializer = new JsonDeserializer<>(kafkaObjectMapper());
        HashMap<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, jsonDeserializer);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP.USER_SERVICE);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configMap.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configMap.put(JsonDeserializer.KEY_DEFAULT_TYPE, KafkaTestDto.class);
        configMap.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaTestDto.class);


        return new DefaultKafkaConsumerFactory<>(configMap, new StringDeserializer(), jsonDeserializer);
    }

//    @Bean
//    public Map<String, Object> consumerConfig(){
//
//        return configMap;
//    }

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
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // 한 개의 프로듀서가 한 번에 보낼 수 있는 최대 메시지 개수 = 1개 <- retry시 메시지 순서 보장
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 95000); // send() 메소드를 실행한 뒤 성공,실패를 결정하는 최대 시간. retry * requestTimeOutMs + 여유분
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
