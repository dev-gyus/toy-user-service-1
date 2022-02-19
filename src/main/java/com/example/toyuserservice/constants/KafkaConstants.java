package com.example.toyuserservice.constants;

public class KafkaConstants {
    public static class Topic {
        public static final String UPDATE_USER = "updateUser";
    }

    public static class Key {

        public static final String UPDATE_OBJECT = "update";
        public static final String USER_ID = "userId";
    }
}
