package com.MindHub.HomeBanking.dtos;

import com.MindHub.HomeBanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
    private long id;

    private String name;

    private int maxAmount;

    private int percentIncrease;
    private List<Integer> payments = new ArrayList<>();

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.percentIncrease = loan.getPercentIncrease();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPercentIncrease() {
        return percentIncrease;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
