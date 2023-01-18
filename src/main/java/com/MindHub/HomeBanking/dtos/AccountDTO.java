package com.MindHub.HomeBanking.dtos;

import com.MindHub.HomeBanking.enums.AccountType;
import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;

    private Boolean active;
    private AccountType accountType;
    private String number;

    private LocalDateTime creationDate;

    private double balance;
    private Set<TransactionDTO> transactions;
    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.active = account.getActive();
    }

    public Boolean getActive() {
        return active;
    }

    public long getId() {
        return id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
