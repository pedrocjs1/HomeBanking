package com.MindHub.HomeBanking.services;

import com.MindHub.HomeBanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    public Set<Transaction> getTransactionByDate (LocalDateTime dateFrom, LocalDateTime dateTo,  Set<Transaction> transactionsAuth);
}
