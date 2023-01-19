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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.MindHub.HomeBanking.Utils.utils.randomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAllAccountDTO();
    };
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountById(id);
    };

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createNewAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client clientCurrent = clientService.getClientCurrent(authentication);
        Set<Account> accountActive = clientCurrent.getAccounts().stream().filter(account -> account.getActive() == true).collect(Collectors.toSet());

        if (accountActive.size() >= 3) {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account("VIN" + randomNumber(10000000, 99999999), LocalDateTime.now(), 0, clientCurrent, accountType, true);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    };

    @Transactional
    @PatchMapping("/clients/current/accounts/disabled")
    public ResponseEntity<Object> disabledAccount(Authentication authentication,
                                                  @RequestParam Long idAccount,
                                                  @RequestParam String password) {
        Client client = clientService.getClientCurrent(authentication);
        Account account = accountService.getAccountByIdOrElse(idAccount);

        if (!passwordEncoder.matches(password, client.getPassword())){
            return new ResponseEntity<>("Current password incorrect", HttpStatus.FORBIDDEN);
        }
        if(idAccount.toString().isEmpty()) {
            return new ResponseEntity<>("Missing ID account", HttpStatus.FORBIDDEN);
        }
        if (idAccount <= 0) {
            return new ResponseEntity<>("Invalid ID account", HttpStatus.FORBIDDEN);
        }
        if (!account.getActive()) {
            return new ResponseEntity<>("Account already deactivated", HttpStatus.FORBIDDEN);
        }
        if (account.getCards().size() > 0) {
            return new ResponseEntity<>("Your account has an associated card, you cannot delete this account.", HttpStatus.FORBIDDEN);
        }
        if(!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() > 0) {
            return new ResponseEntity<>("Your account has balance available, please transfer these balance", HttpStatus.FORBIDDEN);
        }
        account.setActive(false);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account disabled successfully", HttpStatus.ACCEPTED);
    }


}
