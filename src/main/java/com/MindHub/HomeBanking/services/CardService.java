package com.MindHub.HomeBanking.services;

import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CardService {

    Set<Card> getAllCardsAuthenticated(Authentication authentication);
    Card findById(long id);
    void saveCard(Card card);
    public Card findByNumber(String number);
}
