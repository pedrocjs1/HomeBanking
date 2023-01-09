package com.MindHub.HomeBanking.services;

import com.MindHub.HomeBanking.dtos.AccountDTO;
import com.MindHub.HomeBanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAllAccountDTO();

    AccountDTO getAccountById(Long id);

    Account getAccountByNumber(String number);

    void saveAccount(Account account);
}
