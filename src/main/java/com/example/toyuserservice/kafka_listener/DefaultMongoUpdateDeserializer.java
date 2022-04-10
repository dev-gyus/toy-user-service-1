package com.example.toyuserservice.kafka_listener;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.support.JacksonUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Locale;

@Slf4j
public class DefaultMongoUpdateDeserializer<T> extends StdDeserializer<T> {
    private final ObjectMapper defaultObjectMapper = JacksonUtils.enhancedObjectMapper();

    public DefaultMongoUpdateDeserializer(Class<?> clazz){
        super(clazz);
    }


    public T deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        ObjectCodec codec = parser.getCodec();
        TreeNode treeNode = codec.readTree(parser);
        try {
            Object child = Class.forName(treeNode.get("child").toString().replaceAll("\"", "")).getConstructor().newInstance();
            log.info("child info. info:{} / instance of User ? : {}", Arrays.toString(child.getClass().getDeclaredFields()), child instanceof KafkaTestDto.User);
            if(child instanceof KafkaTestDto.User) {
                KafkaTestDto.User convertedDto = (KafkaTestDto.User) child;
                for(Field field : convertedDto.getClass().getDeclaredFields()) {
                    if(field.getName().contains("update")) {
                        continue;
                    }
                    field.setAccessible(true);
                    TreeNode convertedNode = treeNode.get(field.getName());
                    if(convertedNode == null) continue;
                    if(convertedNode.isObject()) {
                        if(field.getName().contains("document")) {
                            convertedDto.setDocument(Document.parse(convertedNode.toString()));
                            convertedDto.setUpdate(Update.fromDocument(convertedDto.getDocument()));
                        }
                    }
                    else if(convertedNode.isValueNode()){
                        JsonNode valueNode = (JsonNode) convertedNode;
                        if(field.getName().toLowerCase(Locale.ROOT).contains("id")) {
                            field.set(convertedDto, valueNode.longValue());
                        }
                        else {
                            field.set(convertedDto, valueNode.asText());
                        }
                    }
                    else if(convertedNode.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) convertedNode;
                    }
                }
                return (T) convertedDto;
            }
            else {
                KafkaTestDto.User2 convertedDto = (KafkaTestDto.User2) child;
                for(Field field : convertedDto.getClass().getDeclaredFields()) {
                    if(field.getName().contains("update")) {
                        continue;
                    }
                    field.setAccessible(true);
                    TreeNode convertedNode = treeNode.get(field.getName());
                    if(convertedNode == null) continue;
                    if(convertedNode.isObject()) {
                        if(field.getName().contains("document")) {
                            convertedDto.setDocument(Document.parse(convertedNode.toString()));
                            convertedDto.setUpdate(Update.fromDocument(convertedDto.getDocument()));
                        }
                    }
                    else if(convertedNode.isValueNode()){
                        JsonNode valueNode = (JsonNode) convertedNode;
                        if(field.getName().toLowerCase(Locale.ROOT).contains("id")) {
                            field.set(convertedDto, valueNode.longValue());
                        }
                        else {
                            field.set(convertedDto, valueNode.asText());
                        }
                    }
                    else if(convertedNode.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) convertedNode;
                    }
                }
                return (T) convertedDto;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
