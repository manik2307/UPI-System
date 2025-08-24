package com.bank.b.dto;

import java.math.BigDecimal;

public class BankTransactionRequest {
    private String accountNumber;
    private BigDecimal amount;
    private String type;
    private String transactionId;
    
    public BankTransactionRequest() {}
    
    public BankTransactionRequest(String accountNumber, BigDecimal amount, String type, String transactionId) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.transactionId = transactionId;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}