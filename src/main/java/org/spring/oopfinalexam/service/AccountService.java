package org.spring.oopfinalexam.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import org.spring.oopfinalexam.model.Transaction;
import org.spring.oopfinalexam.repository.AccountRepository;
import org.spring.oopfinalexam.repository.TransactionRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactionsForAccount(UUID accountId) {
        requireAccount(accountId);
        return transactionRepository.findByAccountId(accountId);
    }

    public BigDecimal getBalance(UUID accountId) {
        requireAccount(accountId);
        return transactionRepository.calculateBalance(accountId);
    }

    private void requireAccount(UUID accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new NoSuchElementException("Account not found: " + accountId);
        }
    }
}
