package com.example.toyuserservice.config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoDbConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.uri}")
    private String dbUri;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(dbUri);
    }

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.data.mongodb.database")
    public MongoTemplate mongoTemplate(){
        DefaultDbRefResolver resolver = new DefaultDbRefResolver(mongoDbFactory());
        MongoMappingContext mongoMappingContext = new MongoMappingContext();

        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(resolver, mongoMappingContext);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingMongoConverter.afterPropertiesSet();

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), mappingMongoConverter);
        mongoTemplate.setWriteResultChecking(WriteResultChecking.NONE);
        return mongoTemplate;
    }

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.data.mongodb.uri")
    public MongoTransactionManager mongoTransactionManager(){
        return new MongoTransactionManager(mongoDbFactory(),
                TransactionOptions.builder()
                        .readPreference(ReadPreference.primary())
                        .readConcern(ReadConcern.MAJORITY)
                        .writeConcern(WriteConcern.MAJORITY)
                        .maxCommitTime(60L, TimeUnit.SECONDS)
                        .build());
    }
}
