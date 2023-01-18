package com.MindHub.HomeBanking.models;


import com.MindHub.HomeBanking.enums.AccountType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private AccountType accountType;
    private Boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="owner_id") // cambiar el nombre de la columna
    private Client owner;
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>(); // es el que guarda la memoria para ese set
    private String number;
    LocalDateTime creationDate = LocalDateTime.now();
    private double balance;
    public Account() { }
    public Account(String number, LocalDateTime creationDate, double balance, Client client, AccountType accountType, Boolean active) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.owner = client;
        this.accountType = accountType;
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public Set<Transaction> getTransactions() {
        return transactions;
    }
    public Long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public void getNumber(String number) {
        this.number = number;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public Client getOwner() {
        return owner;
    }
    public void setOwner(Client owner) {
        this.owner = owner;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}
