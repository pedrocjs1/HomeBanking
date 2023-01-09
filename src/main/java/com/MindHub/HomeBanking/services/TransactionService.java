package com.MindHub.HomeBanking.services;

import com.MindHub.HomeBanking.models.Transaction;

public interface TransactionService {

    void saveTransaction(Transaction transaction);
}
