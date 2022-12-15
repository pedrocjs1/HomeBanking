package com.MindHub.HomeBanking.dtos;

import com.MindHub.HomeBanking.models.Client;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Set<AccountDTO> accountDTO;


    private List<ClientLoanDTO> loans;

    private Set<CardDTO> cards;

    private long id;

    private String firstName, lastName, email;




    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accountDTO = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getLoans();
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());

    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccountDTO(){
        return accountDTO;
    }



}
