//package com.example.bankapp.Kafka;
//
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaProducer {
//
//    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;
//
//    public KafkaProducer(KafkaTemplate<String, TransferEvent> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendTransferEvent(TransferEvent event) {
//        kafkaTemplate.send("transfer-events", event);
//        System.out.println("Event published: " + event);
//    }
//}