package com.upi.central.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionStatusResponse {
    private String transactionId;
    private String status;
    private BigDecimal amount;
    private String senderUpiId;
    private String receiverUpiId;
    private LocalDateTime createdAt;
    
    public TransactionStatusResponse(String transactionId, String status, BigDecimal amount,
                                   String senderUpiId, String receiverUpiId, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
        this.senderUpiId = senderUpiId;
        this.receiverUpiId = receiverUpiId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getSenderUpiId() { return senderUpiId; }
    public void setSenderUpiId(String senderUpiId) { this.senderUpiId = senderUpiId; }
    
    public String getReceiverUpiId() { return receiverUpiId; }
    public void setReceiverUpiId(String receiverUpiId) { this.receiverUpiId = receiverUpiId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}