package com.upi.central.service;

import com.upi.central.entity.Transaction;
import com.upi.central.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Async
    public void sendTransactionNotification(User sender, User receiver, Transaction transaction) {
        // Simulate SMS/Email notification
        logger.info("Sending notification to sender {}: Transaction {} completed for amount {}", 
                   sender.getPhoneNumber(), transaction.getTransactionId(), transaction.getAmount());
        
        logger.info("Sending notification to receiver {}: Received {} from {} in transaction {}", 
                   receiver.getPhoneNumber(), transaction.getAmount(), sender.getName(), transaction.getTransactionId());
    }
    
    @Async
    public void sendFailureNotification(String phoneNumber, String transactionId, String reason) {
        logger.warn("Sending failure notification to {}: Transaction {} failed - {}", 
                   phoneNumber, transactionId, reason);
    }
}