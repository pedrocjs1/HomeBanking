package com.MindHub.HomeBanking.dtos;


import com.MindHub.HomeBanking.models.ClientLoan;

import java.util.Date;

public class ClientLoanDTO {

    private long id;
    private long idLoan;
    private String name;

    private Date date;
    private Integer payments;
    private double amount;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.idLoan = clientLoan.getLoan().getId();
        this.date = clientLoan.getDate();
        this.name = clientLoan.getLoan().getName();
        this.payments = clientLoan.getPayment();
        this.amount = clientLoan.getAmount();
    }

    public long getId() {
        return id;
    }

    public Integer getPayments() {
        return payments;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public long getIdLoan() {
        return idLoan;
    }
}
