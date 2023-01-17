package com.MindHub.HomeBanking.controllers;

import com.MindHub.HomeBanking.dtos.LoanApplicationDTO;
import com.MindHub.HomeBanking.dtos.LoanDTO;
import com.MindHub.HomeBanking.enums.TransactionType;
import com.MindHub.HomeBanking.models.*;
import com.MindHub.HomeBanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    public Set<LoanDTO> getClientsLoans() {
        return loanService.getAllLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplication) {
        Client client = clientService.getClientCurrent(authentication);

        Double amount = loanApplication.getAmount();
        Integer payment = loanApplication.getPayment();
        String targetAccountNumber = loanApplication.getTargetAccount();
        Loan loan = loanService.getLoanById(loanApplication.getId());
        Set<Loan> allLoans = loanService.getAllLoans();

        Set<Loan> loansTaked = client.getLoans().stream().map(clientLoanDTO -> loanService.getLoanById(clientLoanDTO.getIdLoan())).collect(Collectors.toSet());
        Account targetAccount = accountService.getAccountByNumber(targetAccountNumber);

        //IF AMOUNT OR PAYMENT ITS EMPTY
        if (amount == 0 || amount.toString().isEmpty() || payment == 0 || payment.toString().isEmpty() || loanApplication.getTargetAccount().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //IF AMOUNT ITS NEGATIVE (!)
        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
       //IF THE LOAN NOT EXIST
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
      // IF TARGET ACCOUNT NOT EXIST IN DATABASE
        if (!client.getAccounts().contains(targetAccount)) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }
       // IF THE CLIENT AUTHENTICATED NOT OWNER OF THE TARGET ACCOUNT
        if (!client.getAccounts().contains(targetAccount)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(amount * (loan.getPercentIncrease() * 0.01 + 1), payment,  client, loan);
        clientLoanService.saveClientLoan(clientLoan);



        targetAccount.setBalance(targetAccount.getBalance() + amount);
        accountService.saveAccount(targetAccount);

        Transaction transaction = new Transaction(TransactionType.CREDIT, amount, loan.getName() + " loan approved", LocalDateTime.now(), targetAccount, targetAccount.getBalance());
        transactionService.saveTransaction(transaction);

        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);
    }

}
