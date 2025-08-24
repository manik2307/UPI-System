package com.bank.b.controller;

import com.bank.b.dto.BankTransactionRequest;
import com.bank.b.dto.BankTransactionResponse;
import com.bank.b.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    
    @Autowired
    private BankService bankService;
    
    @PostMapping("/transaction")
    public ResponseEntity<BankTransactionResponse> processTransaction(@RequestBody BankTransactionRequest request) {
        return ResponseEntity.ok(bankService.processTransaction(request));
    }
    
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BankTransactionResponse> getBalance(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankService.getBalance(accountNumber));
    }
}
