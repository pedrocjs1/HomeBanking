package com.MindHub.HomeBanking.controllers;


import com.MindHub.HomeBanking.dtos.AccountDTO;
import com.MindHub.HomeBanking.enums.AccountType;
import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.repositories.AccountRepository;
import com.MindHub.HomeBanking.repositories.ClientRepository;
import com.MindHub.HomeBanking.services.AccountService;
import com.MindHub.HomeBanking.services.ClientService;
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
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAllAccountDTO();
    };
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountById(id);
    };

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createNewAccount(Authentication authentication) {
        Client clientCurrent = clientService.getClientCurrent(authentication);

        if (clientCurrent.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account("VIN" + randomNumber(10000000, 99999999), LocalDateTime.now(), 0, clientCurrent);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    };



}
