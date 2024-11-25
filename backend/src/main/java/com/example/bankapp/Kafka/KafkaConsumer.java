//package com.example.bankapp.Kafka;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaConsumer {
//
//    @KafkaListener(topics = "transfer-events", groupId = "transfer-group")
//    public void listenTransferEvents(@Payload TransferEvent event) {
//        System.out.println("Received event: " + event);
//
//    }
//}
