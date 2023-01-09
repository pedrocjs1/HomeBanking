package com.MindHub.HomeBanking.services.implement;

import com.MindHub.HomeBanking.models.Transaction;
import com.MindHub.HomeBanking.repositories.TransactionRepository;
import com.MindHub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
