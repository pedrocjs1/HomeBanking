package com.MindHub.HomeBanking.controllers;


import com.MindHub.HomeBanking.dtos.ClientDTO;
import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.services.AccountService;
import com.MindHub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import static com.MindHub.HomeBanking.Utils.utils.randomNumber;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClientDTO();
    };
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientById(id);
    };

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing firstName", HttpStatus.FORBIDDEN);
        }

        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing lastName", HttpStatus.FORBIDDEN);
        }

        if (email.isEmpty()) {
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);
        }

        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing password", HttpStatus.FORBIDDEN);
        }

        if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        Account account = new Account("VIN" + randomNumber(10000000, 99999999), LocalDateTime.now(), 0, client);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication) {
        return new ClientDTO(clientService.getClientCurrent(authentication));
    }

}
