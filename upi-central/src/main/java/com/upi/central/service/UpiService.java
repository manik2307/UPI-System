package com.upi.central.service;
import com.upi.central.dto.*;
import com.upi.central.entity.Transaction;
import com.upi.central.entity.User;
import com.upi.central.repository.TransactionRepository;
import com.upi.central.repository.UserRepository;
import com.upi.central.service.external.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UpiService {
    
    private static final Logger logger = LoggerFactory.getLogger(UpiService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private BankService bankService;
    
    @Autowired
    private NotificationService notificationService;
    
    public ApiResponse registerUser(UserRegistrationRequest request) {
        try {
            // Check if user already exists
            if (userRepository.existsByUpiId(request.getUpiId()) || 
                userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                return new ApiResponse(false, "User already exists with this UPI ID or phone number");
            }
            
            User user = new User(request.getUpiId(), request.getPhoneNumber(), 
                               request.getName(), request.getBankCode(), request.getAccountNumber());
            userRepository.save(user);
            
            logger.info("User registered successfully: {}", request.getUpiId());
            return new ApiResponse(true, "User registered successfully");
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            return new ApiResponse(false, "Registration failed: " + e.getMessage());
        }
    }
    
    public UserValidationResponse validateUser(String identifier) {
        try {
            Optional<User> user = identifier.contains("@") ? 
                userRepository.findByUpiId(identifier) : 
                userRepository.findByPhoneNumber(identifier);
            
            if (user.isPresent()) {
                User u = user.get();
                return new UserValidationResponse(true, u.getName(), u.getUpiId(), u.getPhoneNumber());
            }
            return new UserValidationResponse(false, null, null, null);
        } catch (Exception e) {
            logger.error("Error validating user: {}", e.getMessage());
            return new UserValidationResponse(false, null, null, null);
        }
    }
    
    @Transactional
    public TransactionResponse transferMoney(TransferRequest request) {
        String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        try {
            // Validate sender and receiver
            Optional<User> sender = userRepository.findByUpiId(request.getSenderUpiId());
            Optional<User> receiver = userRepository.findByUpiId(request.getReceiverUpiId());
            
            if (sender.isEmpty() || receiver.isEmpty()) {
                return new TransactionResponse(false, transactionId, "Invalid sender or receiver UPI ID");
            }
            
            // Create transaction record
            Transaction transaction = new Transaction(transactionId, request.getSenderUpiId(), 
                                                    request.getReceiverUpiId(), request.getAmount(), 
                                                    request.getDescription());
            transactionRepository.save(transaction);
            
            // Process debit from sender's bank
            BankTransactionRequest debitRequest = new BankTransactionRequest(
                sender.get().getAccountNumber(), request.getAmount(), "DEBIT", transactionId);
            BankTransactionResponse debitResponse = bankService.processTransaction(sender.get().getBankCode(), debitRequest);
            
            if (!debitResponse.isSuccess()) {
                transaction.setStatus("FAILED");
                transaction.setUpdatedAt(LocalDateTime.now());
                transactionRepository.save(transaction);
                return new TransactionResponse(false, transactionId, "Insufficient balance or debit failed");
            }
            
            // Process credit to receiver's bank
            BankTransactionRequest creditRequest = new BankTransactionRequest(
                receiver.get().getAccountNumber(), request.getAmount(), "CREDIT", transactionId);
            BankTransactionResponse creditResponse = bankService.processTransaction(receiver.get().getBankCode(), creditRequest);
            
            if (!creditResponse.isSuccess()) {
                // Rollback debit
                BankTransactionRequest rollbackRequest = new BankTransactionRequest(
                    sender.get().getAccountNumber(), request.getAmount(), "CREDIT", transactionId + "_ROLLBACK");
                bankService.processTransaction(sender.get().getBankCode(), rollbackRequest);
                
                transaction.setStatus("FAILED");
                transaction.setUpdatedAt(LocalDateTime.now());
                transactionRepository.save(transaction);
                return new TransactionResponse(false, transactionId, "Credit failed, transaction rolled back");
            }
            
            // Update transaction status
            transaction.setStatus("SUCCESS");
            transaction.setUpdatedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            
            // Send notifications
            notificationService.sendTransactionNotification(sender.get(), receiver.get(), transaction);
            
            logger.info("Transaction completed successfully: {}", transactionId);
            return new TransactionResponse(true, transactionId, "Transaction completed successfully");
            
        } catch (Exception e) {
            logger.error("Transaction failed: {}", e.getMessage());
            
            // Update transaction status to failed
            Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionId);
            if (transaction.isPresent()) {
                transaction.get().setStatus("FAILED");
                transaction.get().setUpdatedAt(LocalDateTime.now());
                transactionRepository.save(transaction.get());
            }
            
            return new TransactionResponse(false, transactionId, "Transaction failed: " + e.getMessage());
        }
    }
    
    public TransactionStatusResponse getTransactionStatus(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionId);
        if (transaction.isPresent()) {
            Transaction t = transaction.get();
            return new TransactionStatusResponse(t.getTransactionId(), t.getStatus(), t.getAmount(), 
                                               t.getSenderUpiId(), t.getReceiverUpiId(), t.getCreatedAt());
        }
        return new TransactionStatusResponse(transactionId, "NOT_FOUND", null, null, null, null);
    }
    
    public TransactionHistoryResponse getTransactionHistory(String upiId) {
        List<Transaction> transactions = transactionRepository
            .findBySenderUpiIdOrReceiverUpiIdOrderByCreatedAtDesc(upiId, upiId);
        
        List<TransactionDto> transactionDtos = transactions.stream()
            .map(t -> new TransactionDto(t.getTransactionId(), t.getSenderUpiId(), t.getReceiverUpiId(),
                                       t.getAmount(), t.getStatus(), t.getDescription(), t.getCreatedAt()))
            .collect(Collectors.toList());
        
        return new TransactionHistoryResponse(transactionDtos);
    }
}