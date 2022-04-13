package com.example.toyuserservice.kafka_listener;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.IOException;

@Slf4j
@NoArgsConstructor
public class BsonDocumentDeserializer extends JsonDeserializer<Document> {

    public Document deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        ObjectCodec codec = parser.getCodec();
        JsonNode treeNode = codec.readTree(parser);
        log.info("jsonParser = {} / jsonNode:{}", parser, treeNode);
        return Document.parse(treeNode.toString());
    }
}
