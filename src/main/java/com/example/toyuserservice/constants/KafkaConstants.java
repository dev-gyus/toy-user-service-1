package com.example.toyuserservice.constants;

public class KafkaConstants {
    public static  class CONSUMER_GROUP {

        public static final String USER_SERVICE = "user-service";
    }

    public static class Topic {
        public static final String UPDATE_USER = "updateUser";
        public static final String UPDATE_USER2 = "updateUser2";
    }

    public static class Key {

        public static final String UPDATE_OBJECT = "update";
        public static final String DOCUMENT = "document";
        public static final String USER_ID = "userId";
    }
}
