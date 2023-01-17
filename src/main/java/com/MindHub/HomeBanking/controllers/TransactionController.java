package com.MindHub.HomeBanking.controllers;

import com.MindHub.HomeBanking.enums.TransactionType;
import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.models.Transaction;
import com.MindHub.HomeBanking.services.AccountService;
import com.MindHub.HomeBanking.services.ClientService;
import com.MindHub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam String description,
                                                    @RequestParam Double amount,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String targetAccountNumber) {

        Client client = clientService.getClientCurrent(authentication);
        Account originAccount = accountService.getAccountByNumber(originAccountNumber);
        Account targetAccount = accountService.getAccountByNumber(targetAccountNumber);

        if (description.isEmpty() || amount == null || amount.isNaN() || originAccountNumber.isEmpty() || targetAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        if (originAccountNumber.equals(targetAccountNumber)) {
            return new ResponseEntity<>("Cannot transfer to own account", HttpStatus.FORBIDDEN);
        }

        if (originAccount == null) {
            return new ResponseEntity<>("Invalid origin account", HttpStatus.FORBIDDEN);
        }

        if (targetAccount == null) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(originAccount)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }

        if (originAccount.getBalance() < amount) {
            return new ResponseEntity<>("You don't have enough balance", HttpStatus.FORBIDDEN);
        }

        if (originAccount == targetAccount) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }

        originAccount.setBalance(originAccount.getBalance() - amount);
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,
                amount,
                "Transfer sent to " + targetAccountNumber + " - " + description,
                LocalDateTime.now(),
                originAccount,
                originAccount.getBalance());

        targetAccount.setBalance(targetAccount.getBalance() + amount);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,
                amount,
                "Transfer received from " + originAccountNumber + " - " + description,
                LocalDateTime.now(),
                targetAccount,
                targetAccount.getBalance());

        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);

        accountService.saveAccount(originAccount);
        accountService.saveAccount(targetAccount);

        return new ResponseEntity<>("Transaction success", HttpStatus.CREATED);

    }

}
