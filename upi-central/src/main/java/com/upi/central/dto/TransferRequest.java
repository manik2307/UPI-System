package com.upi.central.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private String senderUpiId;
    private String receiverUpiId;
    private BigDecimal amount;
    private String description;
    
    // Constructors, Getters, and Setters
    public TransferRequest() {}
    
    public TransferRequest(String senderUpiId, String receiverUpiId, BigDecimal amount, String description) {
        this.senderUpiId = senderUpiId;
        this.receiverUpiId = receiverUpiId;
        this.amount = amount;
        this.description = description;
    }
    
    public String getSenderUpiId() { return senderUpiId; }
    public void setSenderUpiId(String senderUpiId) { this.senderUpiId = senderUpiId; }
    
    public String getReceiverUpiId() { return receiverUpiId; }
    public void setReceiverUpiId(String receiverUpiId) { this.receiverUpiId = receiverUpiId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
