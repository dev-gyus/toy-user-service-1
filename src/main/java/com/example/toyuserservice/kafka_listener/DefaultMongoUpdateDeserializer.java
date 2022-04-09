package com.example.toyuserservice.kafka_listener;

import com.example.toyuserservice.constants.KafkaConstants.Key;
import com.example.toyuserservice.domain.common.ErrorCode;
import com.example.toyuserservice.exception.CustomException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.support.JacksonUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class DefaultMongoUpdateDeserializer extends StdDeserializer<KafkaTestDto> {
    private final ObjectMapper defaultObjectMapper = JacksonUtils.enhancedObjectMapper();

    public DefaultMongoUpdateDeserializer(Class<? extends KafkaTestDto> clazz){
        super(clazz);
    }


    @Override
    public KafkaTestDto deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        KafkaTestDto dto = new KafkaTestDto();
        ObjectCodec codec = parser.getCodec();
        TreeNode treeNode = codec.readTree(parser);

        try {
            for(Field field : KafkaTestDto.class.getDeclaredFields()) {
                if(field.getType().isAssignableFrom(Update.class)) {
                    continue;
                }
                field.setAccessible(true);
                TreeNode node2 = treeNode.get(field.getName());
                if(node2 == null) {
                    continue;
                }
                if(field.getType().isAssignableFrom(Document.class)) {
                    // field가 null인 경우 x
                    if (field.getName().equals(Key.DOCUMENT)) {
                        field.set(dto, Document.parse(node2.toString()));
                    }
                }
                else {
                    field.set(dto, defaultObjectMapper.readValue(node2.traverse(), field.getType()));
                }
            }
            dto.setUpdate(dto.getDocument() != null ? Update.fromDocument(dto.getDocument()) : null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }
}
