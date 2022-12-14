package com.MindHub.HomeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private int percentIncrease;
    private int maxAmount;
    @ElementCollection
    @Column(name = "payments")
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    public Loan() {
    }
    public Loan (String name, int maxAmount, List<Integer> payments, int percentIncrease) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentIncrease = percentIncrease;
    }
    @JsonIgnore
    public Set<ClientLoan> getClientLoan() {
        return clientLoans;
    }
    @JsonIgnore
    public List<Client> getClients() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(toList());
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public int getPercentIncrease() {
        return percentIncrease;
    }
    public int getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}
