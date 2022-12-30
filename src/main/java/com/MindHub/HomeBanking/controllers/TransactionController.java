package com.MindHub.HomeBanking.controllers;


import com.MindHub.HomeBanking.enums.TransactionType;
import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.models.Transaction;
import com.MindHub.HomeBanking.repositories.AccountRepository;
import com.MindHub.HomeBanking.repositories.CardRepository;
import com.MindHub.HomeBanking.repositories.ClientRepository;
import com.MindHub.HomeBanking.repositories.TransactionRepository;
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
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam String description,
                                                    @RequestParam Double amount,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String targetAccountNumber) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(originAccountNumber);
        Account targetAccount = accountRepository.findByNumber(targetAccountNumber);

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
                originAccount);

        targetAccount.setBalance(targetAccount.getBalance() + amount);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,
                amount,
                "Transfer received from " + originAccountNumber + " - " + description,
                LocalDateTime.now(),
                targetAccount);

        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);

        accountRepository.save(originAccount);
        accountRepository.save(targetAccount);

        return new ResponseEntity<>("Transaction success", HttpStatus.CREATED);

    }

}
