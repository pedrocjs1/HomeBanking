package com.MindHub.HomeBanking.services.implement;

import com.MindHub.HomeBanking.dtos.ClientDTO;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.repositories.ClientRepository;
import com.MindHub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getAllClientDTO() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client getClientCurrent(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }
}
