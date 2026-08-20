package org.spring.oopfinalexam.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.spring.oopfinalexam.model.Transaction;
import org.spring.oopfinalexam.service.AccountService;
import org.spring.oopfinalexam.dto.BalanceDTO;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactionsForAccount(@PathVariable UUID id) {
        return accountService.getTransactionsForAccount(id);
    }

    @GetMapping("/account/{id}/balance")
    public BalanceDTO getBalance(@PathVariable UUID id) {
        BigDecimal balance = accountService.getBalance(id);
        return new BalanceDTO(id, balance);
    }
}
