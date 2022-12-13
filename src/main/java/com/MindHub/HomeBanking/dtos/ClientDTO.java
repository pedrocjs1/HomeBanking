package com.MindHub.HomeBanking.dtos;

import com.MindHub.HomeBanking.models.Client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Set<AccountDTO> accountDTO = new HashSet<>();

    private List<ClientLoanDTO> loans;
    long id;

    String firstName, lastName, email;




    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accountDTO = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getLoans();
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

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccountDTO(){
        return accountDTO;
    }



}
