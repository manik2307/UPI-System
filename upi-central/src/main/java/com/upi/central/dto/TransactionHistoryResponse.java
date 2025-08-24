package com.upi.central.dto;

import java.util.List;

public class TransactionHistoryResponse {
    private List<TransactionDto> transactions;
    
    public TransactionHistoryResponse(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }
    
    public List<TransactionDto> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionDto> transactions) { this.transactions = transactions; }
}