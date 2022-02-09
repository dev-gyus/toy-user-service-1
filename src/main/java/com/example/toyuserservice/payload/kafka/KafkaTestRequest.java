package com.example.toyuserservice.payload.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaTestRequest {
    private String message;
    private String message2;
}
