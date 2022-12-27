package com.MindHub.HomeBanking.controllers;


import com.MindHub.HomeBanking.dtos.AccountDTO;
import com.MindHub.HomeBanking.enums.AccountType;
import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.repositories.AccountRepository;
import com.MindHub.HomeBanking.repositories.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.MindHub.HomeBanking.Utils.utils.randomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    };
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    };

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createNewAccount(Authentication authentication) {
        Client clientCurrent = clientRepository.findByEmail(authentication.getName());

        if (clientCurrent.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account("VIN" + randomNumber(10000000, 99999999), LocalDateTime.now(), 0, clientCurrent);
        accountRepository.save(newAccount);

        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    };



}
