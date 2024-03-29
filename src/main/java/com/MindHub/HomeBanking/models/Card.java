package com.MindHub.HomeBanking.models;

import com.MindHub.HomeBanking.enums.CardType;
import com.MindHub.HomeBanking.enums.ColorCard;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardHolder;
    private String account;
    private CardType type;
    private ColorCard color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private Boolean disable;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="card_id")
    private Account associatedAccount;

    public Card() {
    }
    public Card(String cardHolder,
                CardType type,
                ColorCard color,
                String number,
                int cvv,
                LocalDate fromDate,
                LocalDate thruDate,
                Client client,
                Boolean disable,
                Account associatedAccount,
                String account) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
        this.disable = disable;
        this.associatedAccount = associatedAccount;
        this.account = account;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Account getAssociatedAccount() {
        return associatedAccount;
    }

    public void setAssociatedAccount(Account associatedAccount) {
        this.associatedAccount = associatedAccount;
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
    @JsonIgnore
    public Client getClient() {
        return client;
    }
    public CardType getType() {
        return type;
    }
    public void setType(CardType type) {
        this.type = type;
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
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDate getThruDate() {
        return thruDate;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
    public void setColor(ColorCard color) {
        this.color = color;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

}
