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

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClientDTO();
    };
    @GetMapping("/clients/{id}")
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

    @PatchMapping("/clients/current/password")
    public ResponseEntity<?> changePassword(Authentication authentication,
                                            @RequestParam String newPassword,
                                            @RequestParam String password,
                                            @RequestParam String email) {
        Client clientAuth = clientService.getClientCurrent(authentication);
        Client clientEmial = clientService.getClientByEmail(clientAuth.getEmail());

        if (!(clientEmial.getEmail().equals(email))) {
            return new ResponseEntity<>("Email incorrect",HttpStatus.FORBIDDEN);

        }
        if (passwordEncoder.matches(newPassword, clientEmial.getPassword())) {
            return new ResponseEntity<>("You must enter a different password than the previous one",HttpStatus.FORBIDDEN);
        }
        if (newPassword.length() < 5) {
            return new ResponseEntity<>("Password must contain at least 5 characters", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(password, clientEmial.getPassword())){
            return new ResponseEntity<>("Current password incorrect", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        clientAuth.setPassword(passwordEncoder.encode(newPassword));
        clientService.saveClient(clientAuth);
        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }


}
