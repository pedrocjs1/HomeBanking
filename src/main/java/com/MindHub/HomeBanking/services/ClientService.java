package com.MindHub.HomeBanking.services;

import com.MindHub.HomeBanking.dtos.ClientDTO;
import com.MindHub.HomeBanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getAllClientDTO();

    ClientDTO getClientById(Long id);

    Client getClientByEmail(String email);

    void saveClient(Client client);

    Client getClientCurrent(Authentication authentication);
}
