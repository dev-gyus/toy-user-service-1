package com.example.toyuserservice.controller;

import com.example.toyuserservice.config.KafkaConfig;
import com.example.toyuserservice.constants.KafkaConstants;
import com.example.toyuserservice.constants.KafkaConstants.CONSUMER_GROUP;
import com.example.toyuserservice.domain.dao.KafkaInfoDao;
import com.example.toyuserservice.kafka_publisher.KafkaPublisher;
import com.example.toyuserservice.payload.kafka.KafkaTestRequest;
import com.example.toyuserservice.repository.KafkaInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final Environment env;
    private final KafkaPublisher kafkaPublisher;
    private final KafkaInfoRepository kafkaInfoRepository;
    private final ApplicationContext ctx;
    private final KafkaListenerEndpointRegistry registry;
    @GetMapping("/user")
    public ResponseEntity<String> getUser(){
        return new ResponseEntity<>("'username': 'gyus', 'mobile':'10-1234-1234'", HttpStatus.OK);
    }
//
    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        log.info("user-client info: {}", env.getProperty("eureka.instance.instance-id"));
        return new ResponseEntity<>("Hello, this is user-client", HttpStatus.OK);
    }

    @GetMapping("/properties/all")
    public ResponseEntity<List<String>> getProperties(){
        return new ResponseEntity<>(Arrays.asList(env.getProperty("test.token-expire"), env.getProperty("test.salt")), HttpStatus.OK);
    }
//
    @PostMapping("/publish/test")
    public ResponseEntity<Boolean> kafkaPublishTest(@RequestBody KafkaTestRequest kafkaTestRequest){
        log.info("카프카 퍼블리쉬 테스트 메소드 진입");
        kafkaPublisher.publishTest(kafkaTestRequest.getMessage(), kafkaTestRequest.getMessage2());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Boolean> kafkaRefresh(Long originIpId, String modifyIps) {
        log.info("카프카 리프레쉬");
        KafkaInfoDao dao = kafkaInfoRepository.findByKafkaInfoId(originIpId);
        log.info("KafkaInfo : {}", dao.getInfo());

        ConcurrentKafkaListenerContainerFactory<String, Object> bean = ctx.getBean(ConcurrentKafkaListenerContainerFactory.class);
        String originIp = (String) bean.getConsumerFactory().getConfigurationProperties().get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
        log.info("originIp : {}", originIp);

        HashMap<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, modifyIps);
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP.USER_SERVICE);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        bean.getConsumerFactory().updateConfigs(configMap);

        HashMap<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, modifyIps);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        KafkaTemplate<String, Object> template = ctx.getBean(KafkaTemplate.class);
        template.getProducerFactory().updateConfigs(config);
        template.getProducerFactory().reset();

        registry.stop();
        registry.setApplicationContext(ctx);
        registry.start();


        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @GetMapping("/post/refresh")
    public ResponseEntity<Boolean> kafkaPostRefresh() {
        log.info("카프카 리프레쉬");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(KafkaConfig.class);
        ConcurrentKafkaListenerContainerFactory<String, Object> bean = ctx.getBean(ConcurrentKafkaListenerContainerFactory.class);
        String originIp = (String) bean.getConsumerFactory().getConfigurationProperties().get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
        log.info("originIp : {}", originIp);

        KafkaTemplate<String,Object> bean1 = ctx.getBean(KafkaTemplate.class);
        log.info("broker ip : {}", bean1.getProducerFactory().getConfigurationProperties().get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));

        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

}
