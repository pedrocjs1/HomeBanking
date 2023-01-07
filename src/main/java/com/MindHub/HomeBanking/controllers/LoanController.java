package com.MindHub.HomeBanking.controllers;


import com.MindHub.HomeBanking.dtos.ClientLoanDTO;
import com.MindHub.HomeBanking.dtos.LoanApplicationDTO;
import com.MindHub.HomeBanking.dtos.LoanDTO;
import com.MindHub.HomeBanking.enums.TransactionType;
import com.MindHub.HomeBanking.models.*;
import com.MindHub.HomeBanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/loans")
    public Set<LoanDTO> getClientsLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        Double amount = loanApplication.getAmount();
        Integer payment = loanApplication.getPayment();
        String targetAccountNumber = loanApplication.getTargetAccount();
        Loan loan = loanRepository.findById(loanApplication.getId()).orElse(null);
        Set<Loan> allLoans = loanRepository.findAll().stream().collect(Collectors.toSet());

        Set<Loan> loansTaked = client.getLoans().stream().map(clientLoanDTO -> loanRepository.findById(clientLoanDTO.getIdLoan()).orElse(null)).collect(Collectors.toSet());
        Account targetAccount = accountRepository.findByNumber(targetAccountNumber);

        //IF AMOUNT OR PAYMENT ITS EMPTY
        if (amount == 0 || amount.toString().isEmpty() || payment == 0 || payment.toString().isEmpty() || loanApplication.getTargetAccount().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //IF AMOUNT ITS NEGATIVE (!)
        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
//        IF THE LOAN NOT EXIST
        if (!allLoans.contains(loan)) {
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }

        if (loansTaked.contains(loan)) {
            return new ResponseEntity<>("Loan already taked", HttpStatus.FORBIDDEN);
        }

        //IF AMOUNT EXCEDED OF MAX AMOUNT
        if (amount > loan.getMaxAmount()) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        //IF PAYMENT NOT EXIST IN PAYMENTS
        if (!loan.getPayments().contains(payment)) {
            return new ResponseEntity<>("Invalid payment", HttpStatus.FORBIDDEN);
        }
//        // IF TARGET ACCOUNT NOT EXIST IN DATABASE
        if (!client.getAccounts().contains(targetAccount)) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }
//        // IF THE CLIENT AUTHENTICATED NOT OWNER OF THE TARGET ACCOUNT
        if (!client.getAccounts().contains(targetAccount)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(amount * (loan.getPercentIncrease() * 0.01 + 1), payment,  client, loan);
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, amount, loan.getName() + " loan approved", LocalDateTime.now(), targetAccount);
        transactionRepository.save(transaction);

        targetAccount.setBalance(targetAccount.getBalance() + amount);
        accountRepository.save(targetAccount);

        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);
    }

}
