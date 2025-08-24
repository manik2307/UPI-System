package com.bank.b.service;

import com.bank.b.dto.BankTransactionRequest;
import com.bank.b.dto.BankTransactionResponse;
import com.bank.b.entity.Account;
import com.bank.b.entity.BankTransaction;
import com.bank.b.repository.AccountRepository;
import com.bank.b.repository.BankTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankService {
    
    private static final Logger logger = LoggerFactory.getLogger(BankService.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private BankTransactionRepository transactionRepository;
    
    @Transactional
    public BankTransactionResponse processTransaction(BankTransactionRequest request) {
        try {
            Optional<Account> accountOpt = accountRepository.findByAccountNumber(request.getAccountNumber());
            
            if (accountOpt.isEmpty()) {
                return new BankTransactionResponse(false, "Account not found", null);
            }
            
            Account account = accountOpt.get();
            
            if (!"ACTIVE".equals(account.getStatus())) {
                return new BankTransactionResponse(false, "Account is not active", account.getBalance());
            }
            
            if ("DEBIT".equals(request.getType())) {
                if (account.getBalance().compareTo(request.getAmount()) < 0) {
                    return new BankTransactionResponse(false, "Insufficient balance", account.getBalance());
                }
                account.setBalance(account.getBalance().subtract(request.getAmount()));
            } else if ("CREDIT".equals(request.getType())) {
                account.setBalance(account.getBalance().add(request.getAmount()));
            } else {
                return new BankTransactionResponse(false, "Invalid transaction type", account.getBalance());
            }
            
            account.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(account);
            
            // Record transaction
            BankTransaction transaction = new BankTransaction(request.getAccountNumber(), 
                                                            request.getAmount(), request.getType(), request.getTransactionId());
            transactionRepository.save(transaction);
            
            logger.info("Transaction processed successfully: {} {} for account {}", 
                       request.getType(), request.getAmount(), request.getAccountNumber());
            
            return new BankTransactionResponse(true, "Transaction processed successfully", account.getBalance());
            
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", e.getMessage());
            return new BankTransactionResponse(false, "Transaction failed: " + e.getMessage(), null);
        }
    }
    
    public BankTransactionResponse getBalance(String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isPresent()) {
            return new BankTransactionResponse(true, "Balance retrieved", account.get().getBalance());
        }
        return new BankTransactionResponse(false, "Account not found", null);
    }
}