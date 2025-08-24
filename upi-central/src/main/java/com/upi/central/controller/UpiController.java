package com.upi.central.controller;

import com.upi.central.dto.*;
import com.upi.central.service.UpiService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/upi")
public class UpiController {
    
    @Autowired
    private UpiService upiService;
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        Counter.builder("upi.register.requests").register(meterRegistry).increment();
        return ResponseEntity.ok(upiService.registerUser(request));
    }
    
    @GetMapping("/validate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserValidationResponse> validateUser(@RequestParam String identifier) {
        Counter.builder("upi.validate.requests").register(meterRegistry).increment();
        return ResponseEntity.ok(upiService.validateUser(identifier));
    }
    
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionResponse> transferMoney(@RequestBody TransferRequest request) {
        Counter.builder("upi.transfer.requests").register(meterRegistry).increment();
        return ResponseEntity.ok(upiService.transferMoney(request));
    }
    
    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionStatusResponse> getTransactionStatus(@PathVariable String transactionId) {
        return ResponseEntity.ok(upiService.getTransactionStatus(transactionId));
    }
    
    @GetMapping("/transactions/{upiId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionHistoryResponse> getTransactionHistory(@PathVariable String upiId) {
        return ResponseEntity.ok(upiService.getTransactionHistory(upiId));
    }
}
