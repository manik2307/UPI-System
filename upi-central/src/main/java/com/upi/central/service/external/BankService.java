package com.upi.central.service.external;

import com.upi.central.dto.BankTransactionRequest;
import com.upi.central.dto.BankTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${bank.a.url}")
    private String bankAUrl;
    
    @Value("${bank.b.url}")
    private String bankBUrl;
    
    public BankTransactionResponse processTransaction(String bankCode, BankTransactionRequest request) {
        String url = getBankUrl(bankCode) + "/api/bank/transaction";
        
        try {
            return restTemplate.postForObject(url, request, BankTransactionResponse.class);
        } catch (Exception e) {
            return new BankTransactionResponse(false, "Bank service unavailable: " + e.getMessage(), null);
        }
    }
    
    private String getBankUrl(String bankCode) {
        switch (bankCode.toUpperCase()) {
            case "BANK_A": return bankAUrl;
            case "BANK_B": return bankBUrl;
            default: throw new IllegalArgumentException("Unknown bank code: " + bankCode);
        }
    }
}