package com.MindHub.HomeBanking.controllers;

import com.MindHub.HomeBanking.dtos.PaymentsDTO;
import com.MindHub.HomeBanking.dtos.pdfDTO;
import com.MindHub.HomeBanking.enums.TransactionType;
import com.MindHub.HomeBanking.models.*;
import com.MindHub.HomeBanking.services.AccountService;
import com.MindHub.HomeBanking.services.CardService;
import com.MindHub.HomeBanking.services.ClientService;
import com.MindHub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;



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


    @PostMapping("/transactions/pdf")
    public ResponseEntity<Object> getPdfTransactions(@RequestBody pdfDTO pdfDTO,
                                                     Authentication authentication) throws Exception {

        Client clientAuthentication = clientService.getClientCurrent(authentication);
        Account account = accountService.getAccountByNumber(pdfDTO.getAccountNumber());
        Set<Transaction> transactionsAuth = account.getTransactions();

        if (clientAuthentication == null)
            return new ResponseEntity<>("error", HttpStatus.FORBIDDEN);

        if (account == null)
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);

        if ( pdfDTO.getDateFrom() == null || pdfDTO.getDateTo() == null)
            return new ResponseEntity<>("Invalid dates", HttpStatus.FORBIDDEN);

        if (pdfDTO.getDateFrom().isAfter(pdfDTO.getDateTo()))//isAfter es un metodo que se usa para comparar fechas.
            return new ResponseEntity<>("Invalid dates", HttpStatus.FORBIDDEN);

        Set<Transaction> transactions = transactionService.getTransactionByDate(pdfDTO.getDateFrom(), pdfDTO.getDateTo(),transactionsAuth);


        PDFMethod.createPDF(transactions);

        return new ResponseEntity<>("PDF created", HttpStatus.CREATED);

    }

//    @PostMapping("/clients/current/transactions/payments")
//    public ResponseEntity<Object> newPayments(Authentication authentication, @RequestBody PaymentsDTO paymentsDTO){
//        Client client = clientService.getClientByEmail(authentication.getName());
//        Card card = cardService.findById(paymentsDTO.getId());
//        String accountOrigin = card.getAccount();
//        Account accountOriginA = accountService.getAccountByNumber(paymentsDTO.getNumber());
//        Card cardNumber = cardService.findByNumber(paymentsDTO.getNumber());
//
//        if (client == null){
//            return new ResponseEntity<>("client does not exist", HttpStatus.FORBIDDEN);
//        }
//        if (card == null){
//            return new ResponseEntity<>("card does not exist", HttpStatus.FORBIDDEN);
//        }
//
//        if(cardNumber == null){
//            return new ResponseEntity<>("card number does not exist", HttpStatus.FORBIDDEN);
//        }
//
//        if (paymentsDTO.getAmount() <= 0 ){
//            return new ResponseEntity<>("invalid amount", HttpStatus.FORBIDDEN);
//        }
//
//        if(accountOrigin == null){
//            return new ResponseEntity<>("account does not exist", HttpStatus.FORBIDDEN);
//        }
//        if (paymentsDTO.getAmount() > accountOriginA.getBalance()){
//            return new ResponseEntity<>("Amount is invalid", HttpStatus.FORBIDDEN);
//        }
//
//        transactionService.saveTransaction(new Transaction(TransactionType.DEBIT,- paymentsDTO.getAmount(), paymentsDTO.getDescription(),LocalDateTime.now(), accountOriginA, accountOriginA.getBalance()));
//        accountOriginA.setBalance(accountOriginA.getBalance() - paymentsDTO.getAmount());
//        return new ResponseEntity<>("Payment exited",HttpStatus.CREATED);
//    }

}
