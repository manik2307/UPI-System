package com.upi.central.dto;

public class UserValidationResponse {
    private boolean valid;
    private String name;
    private String upiId;
    private String phoneNumber;
    
    public UserValidationResponse(boolean valid, String name, String upiId, String phoneNumber) {
        this.valid = valid;
        this.name = name;
        this.upiId = upiId;
        this.phoneNumber = phoneNumber;
    }
    
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}