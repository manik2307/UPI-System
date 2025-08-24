package com.upi.central.dto;

public class UserRegistrationRequest {
    private String upiId;
    private String phoneNumber;
    private String name;
    private String bankCode;
    private String accountNumber;
    
    // Constructors, Getters, and Setters
    public UserRegistrationRequest() {}
    
    public UserRegistrationRequest(String upiId, String phoneNumber, String name, String bankCode, String accountNumber) {
        this.upiId = upiId;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
    }
    
    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
}