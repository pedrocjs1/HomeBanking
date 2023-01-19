package com.MindHub.HomeBanking.controllers;


import com.MindHub.HomeBanking.enums.CardType;
import com.MindHub.HomeBanking.enums.ColorCard;
import com.MindHub.HomeBanking.models.Account;
import com.MindHub.HomeBanking.models.Card;
import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.repositories.AccountRepository;
import com.MindHub.HomeBanking.repositories.CardRepository;
import com.MindHub.HomeBanking.repositories.ClientRepository;
import com.MindHub.HomeBanking.services.AccountService;
import com.MindHub.HomeBanking.services.CardService;
import com.MindHub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static com.MindHub.HomeBanking.Utils.utils.randomNumber;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam ColorCard colorCard, @RequestParam CardType cardType, @RequestParam String account) {
        Client clientCurrent = clientService.getClientCurrent(authentication);
        Set<Card> cardsClient = cardService.getAllCardsAuthenticated(authentication);
        Set<Card> cardsDebit = cardsClient.stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toSet());
        Set<Card> cardsDebitDisable = cardsDebit.stream().filter(card -> card.getDisable() == false).collect(Collectors.toSet());
        Set<Card> cardsCredit = cardsClient.stream().filter(card -> card.getType() == CardType.CREDIT).collect(Collectors.toSet());
        Set<Card> cardsCreditDisable = cardsCredit.stream().filter(card -> card.getDisable() == false).collect(Collectors.toSet());
        Account associatedAccount = accountService.getAccountByNumber(account);

        if (cardsDebitDisable.size() < 3  && cardType.equals(CardType.DEBIT)) {
            if (cardsDebitDisable.stream().filter(card -> card.getColor().equals(colorCard)).count() == 1) {
                return new ResponseEntity<>("You exceeded the color of cards", HttpStatus.FORBIDDEN);
            } else {
                Card card = new Card(clientCurrent.getFullname(),
                        CardType.DEBIT,
                        colorCard,
                        "5547" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999),
                        randomNumber(100, 999),
                        LocalDate.now(),
                        LocalDate.now().plusYears(5),
                        clientCurrent,
                        false,
                        associatedAccount,
                        account);
                cardService.saveCard(card);
                return new ResponseEntity<>("Created succes", HttpStatus.CREATED);
            }
        } else if (cardsCreditDisable.size() < 3 && cardType.equals(CardType.CREDIT)) {
            if (cardsCreditDisable.stream().filter(card -> card.getColor().equals(colorCard)).count() == 1) {
                return new ResponseEntity<>("You exceeded the color of cards", HttpStatus.FORBIDDEN);
            } else {
                Card card = new Card(clientCurrent.getFullname(),
                        CardType.CREDIT,
                        colorCard,
                        "5547" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999),
                        randomNumber(100, 999),
                        LocalDate.now(),
                        LocalDate.now().plusYears(5),
                        clientCurrent,
                        false,
                        associatedAccount,
                        account);
                cardService.saveCard(card);
                return new ResponseEntity<>("Created succes", HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("Limit cards reached", HttpStatus.FORBIDDEN);
        }

    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> disableCard(Authentication authentication, @RequestParam long id) {
        Card card = cardService.findById(id);
        Client client = clientService.getClientCurrent(authentication);
        if (!client.getCards().contains(card)) {
            return new ResponseEntity<>("This is not your card", HttpStatus.FORBIDDEN);
        }
        card.setDisable(true);
        cardService.saveCard(card);
        return new ResponseEntity<>("Card disabled", HttpStatus.FORBIDDEN);
    }

}
