package com.MindHub.HomeBanking.services.implement;

import com.MindHub.HomeBanking.models.Transaction;
import com.MindHub.HomeBanking.repositories.TransactionRepository;
import com.MindHub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Set<Transaction> getTransactionByDate (LocalDateTime dateFrom, LocalDateTime dateTo, Set<Transaction> transactionsAuth) {
        return transactionsAuth.stream()
                .filter(t -> t.getDate().isAfter(dateFrom) && t.getDate().isBefore(dateTo))
                .collect(Collectors.toSet());
    }
}
