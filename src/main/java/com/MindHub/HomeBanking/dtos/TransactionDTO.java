package com.MindHub.HomeBanking.dtos;


import com.MindHub.HomeBanking.models.Transaction;
import com.MindHub.HomeBanking.enums.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;

    private TransactionType type;

    private double amount;
    private String description;
    private LocalDateTime date;
    private double balance;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
        this.type = transaction.getType();
        this.balance = transaction.getBalance();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


}
