package com.upi.central.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upi.central.dto.ApiResponse;
import com.upi.central.entity.Transaction;
import com.upi.central.entity.User;
import com.upi.central.repository.TransactionRepository;
import com.upi.central.repository.UserRepository;

@Service
public class AdminService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    public ApiResponse disableUser(String upiId) {
        Optional<User> user = userRepository.findByUpiId(upiId);
        if (user.isPresent()) {
            // In a real implementation, you'd have an 'active' field
            return new ApiResponse(true, "User disabled successfully");
        }
        return new ApiResponse(false, "User not found");
    }
} 