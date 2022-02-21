package com.example.toyuserservice.kafka_listener;

import com.example.toyuserservice.constants.KafkaConstants.Key;
import com.example.toyuserservice.domain.common.ErrorCode;
import com.example.toyuserservice.exception.CustomException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class DefaultMongoUpdateDeserializer extends StdDeserializer<KafkaTestDto> {
    private KafkaTestDto dto;

    public DefaultMongoUpdateDeserializer(Class<? extends KafkaTestDto> clazz){
        super(clazz);
        try {
            this.dto = clazz.getDeclaredConstructor(null).newInstance(null);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    public KafkaTestDto deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        ObjectCodec codec = parser.getCodec();
        JsonNode treeNode = codec.readTree(parser);
        try {
            for(Field field : KafkaTestDto.User.class.getDeclaredFields()){
                JsonNode fieldNode = treeNode.get(field.getName());
                field.setAccessible(true);
                // field가 null인 경우 x
                if(!field.getName().equals(Key.UPDATE_OBJECT)) {
                    if(field.getName().contains("Id")) {
                        long idValue = fieldNode.asLong();
                        field.set(dto, idValue);
                    }else{
                        String stringValue = fieldNode.asText();
                        field.set(dto, stringValue);
                    }
                }
                else{
                    field.set(dto, Update.fromDocument(Document.parse(fieldNode.toString())));
                }
            }
        } catch (IllegalAccessException e) {
            throw new CustomException(ErrorCode.GENERIC_INSTANTIATING_FAILED);
        }
        return dto;
    }
}
