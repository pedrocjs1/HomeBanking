package com.MindHub.HomeBanking.dtos;

import com.MindHub.HomeBanking.models.Card;
import com.MindHub.HomeBanking.enums.CardType;
import com.MindHub.HomeBanking.enums.ColorCard;

import java.time.LocalDate;

public class CardDTO {

    private Long id;

    private String cardHolder;

    private CardType type;

    private ColorCard color;

    private String number;

    private int cvv;

    private LocalDate fromDate;

    private LocalDate thruDate;

    private Boolean disable;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.disable = card.getDisable();
    }

    public Boolean getDisable() {
        return disable;
    }
    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public ColorCard getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
