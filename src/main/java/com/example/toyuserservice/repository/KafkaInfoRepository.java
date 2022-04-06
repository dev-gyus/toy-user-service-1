package com.example.toyuserservice.repository;

import com.example.toyuserservice.domain.dao.KafkaInfoDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KafkaInfoRepository extends MongoRepository<KafkaInfoDao, String> {
    KafkaInfoDao findByKafkaInfoId(Long kafkaInfo);
}
