package com.MindHub.HomeBanking.controllers;


import com.MindHub.HomeBanking.enums.CardType;
import com.MindHub.HomeBanking.enums.ColorCard;
import com.MindHub.HomeBanking.models.Card;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.repositories.AccountRepository;
import com.MindHub.HomeBanking.repositories.CardRepository;
import com.MindHub.HomeBanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static com.MindHub.HomeBanking.Utils.utils.randomNumber;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam ColorCard colorCard, @RequestParam CardType cardType) {
        Client clientCurrent = clientRepository.findByEmail(authentication.getName());
        Set<Card> cardsClient = cardRepository.findAll().stream().filter(card -> card.getClient() == clientCurrent).collect(Collectors.toSet());
        Set<Card> cardsDebit = cardsClient.stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toSet());
        Set<Card> cardsCredit = cardsClient.stream().filter(card -> card.getType() == CardType.CREDIT).collect(Collectors.toSet());

        if (cardsDebit.size() < 3 && cardType.equals(CardType.DEBIT)) {
            if (cardsDebit.stream().filter(card -> card.getColor().equals(colorCard)).count() == 1) {
                return new ResponseEntity<>("You exceeded the color of cards", HttpStatus.FORBIDDEN);
            } else {
                Card card = new Card(clientCurrent.getFullname(),
                        CardType.DEBIT,
                        colorCard,
                        "5547" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999),
                        randomNumber(100, 999),
                        LocalDate.now(),
                        LocalDate.now().plusYears(5),
                        clientCurrent);
                cardRepository.save(card);
                return new ResponseEntity<>("Created succes", HttpStatus.CREATED);
            }
        } else if (cardsCredit.size() < 3 && cardType.equals(CardType.CREDIT)) {
            if (cardsCredit.stream().filter(card -> card.getColor().equals(colorCard)).count() == 1) {
                return new ResponseEntity<>("You exceeded the color of cards", HttpStatus.FORBIDDEN);
            } else {
                Card card = new Card(clientCurrent.getFullname(),
                        CardType.CREDIT,
                        colorCard,
                        "5547" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999),
                        randomNumber(100, 999),
                        LocalDate.now(),
                        LocalDate.now().plusYears(5),
                        clientCurrent);
                cardRepository.save(card);
                return new ResponseEntity<>("Created succes", HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("Limit cards reached", HttpStatus.FORBIDDEN);
        }

    }

}
