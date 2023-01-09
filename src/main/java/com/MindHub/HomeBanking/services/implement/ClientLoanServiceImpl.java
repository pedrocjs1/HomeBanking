package com.MindHub.HomeBanking.services.implement;

import com.MindHub.HomeBanking.models.ClientLoan;
import com.MindHub.HomeBanking.repositories.ClientLoanRepository;
import com.MindHub.HomeBanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
