package com.MindHub.HomeBanking.services.implement;

import com.MindHub.HomeBanking.dtos.LoanDTO;
import com.MindHub.HomeBanking.models.Loan;
import com.MindHub.HomeBanking.repositories.LoanRepository;
import com.MindHub.HomeBanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Set<LoanDTO> getAllLoansDTO() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public Set<Loan> getAllLoans() {
        return loanRepository.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
