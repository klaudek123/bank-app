//package com.example.bankapp.Transfer;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TransferListener {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public TransferListener(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @KafkaListener(topics = "transfer-topic", groupId = "notification-group")
//    public void listenToTransferEvent(TransferDto transferDto) {
//        System.out.println("Otrzymano zdarzenie z Kafka: {} "+ transferDto);
//
//        messagingTemplate.convertAndSend("/topic/notifications", transferDto);
//        System.out.println("Wysłano zdarzenie na WebSocket: {} "+ transferDto);
//    }
//}
