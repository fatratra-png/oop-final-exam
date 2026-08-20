package org.spring.oopfinalexam.service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import org.spring.oopfinalexam.model.Transaction;
import org.spring.oopfinalexam.model.TransactionType;
import org.spring.oopfinalexam.repository.AccountRepository;
import org.spring.oopfinalexam.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> getTransactions(TransactionType type) {
        return transactionRepository.findAll(type);
    }

    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getAccountId() == null || !accountRepository.existsById(transaction.getAccountId())) {
            throw new NoSuchElementException("Account not found: " + transaction.getAccountId());
        }
        if (transaction.getTransactionType() == null) {
            throw new IllegalArgumentException("transactionType is required");
        }
        if (transaction.getAmount() == null) {
            throw new IllegalArgumentException("amount is required");
        }

        transaction.setId(UUID.randomUUID());
        transaction.setCreatedAt(Instant.now());
        return transactionRepository.save(transaction);
    }
}
