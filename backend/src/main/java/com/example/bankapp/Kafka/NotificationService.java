//package com.example.bankapp.Kafka;
//
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificationService {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public NotificationService(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    public void notifyUser(TransferEvent event) {
//        messagingTemplate.convertAndSend("/topic/notifications", event);
//    }
//}