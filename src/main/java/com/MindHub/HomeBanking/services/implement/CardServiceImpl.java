package com.MindHub.HomeBanking.services.implement;

import com.MindHub.HomeBanking.models.Card;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.repositories.CardRepository;
import com.MindHub.HomeBanking.services.CardService;
import com.MindHub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;
    @Override
    public Set<Card> getAllCardsAuthenticated(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        return cardRepository.findAll().stream().filter(card -> card.getClient() == client).collect(Collectors.toSet());
    }
    @Override
    public Card findById(long id) {
        return cardRepository.findById(id).orElse(null);
    }
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

}
