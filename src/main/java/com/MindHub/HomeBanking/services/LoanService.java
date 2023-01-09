package com.MindHub.HomeBanking.services;

import com.MindHub.HomeBanking.dtos.LoanDTO;
import com.MindHub.HomeBanking.models.Loan;


import java.util.Set;

public interface LoanService {

    Set<LoanDTO> getAllLoansDTO();

    Loan getLoanById(Long id);

    Set<Loan> getAllLoans();

    void saveLoan(Loan loan);
}
