package com.MindHub.HomeBanking.models;
import com.MindHub.HomeBanking.enums.TransactionType;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private TransactionType type;
    private double  amount;
    private String description;
    private double balance;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account")
    private Account account;
    public Transaction() {}
    public Transaction(TransactionType  type, double  amount, String description, LocalDateTime date, Account account, double balance) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        this.balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }
    public double getBalance() {
        return balance;
    }
    public double  getAmount() {
        return amount;
    }
    public void setAmount(double  amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public TransactionType getType() {
        return type;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }




}
