package com.MindHub.HomeBanking.dtos;

import java.time.LocalDate;

public class PaymentsDTO {
    private long id;

    private String cardHolder;

    private String number;

    private Double amount;

    private Integer cvv;

    private LocalDate thruDate;

    private String description;
    private String destiny;

    public PaymentsDTO() {
    }

    public PaymentsDTO(String number, Double amount, Integer cvv, String description, String destiny) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.amount = amount;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.description = description;
        this.destiny = destiny;
    }

    public String getDestiny() {
        return destiny;
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getDescription() {
        return description;
    }
}