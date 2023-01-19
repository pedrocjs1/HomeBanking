package com.MindHub.HomeBanking.dtos;

import java.time.LocalDateTime;

public class pdfDTO {


    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

    private String accountNumber;

    public pdfDTO(LocalDateTime dateFrom, LocalDateTime dateTo, String accountNumber) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.accountNumber = accountNumber;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}
