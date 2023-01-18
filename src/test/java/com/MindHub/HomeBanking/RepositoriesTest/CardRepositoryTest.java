package com.MindHub.HomeBanking.RepositoriesTest;

import com.MindHub.HomeBanking.enums.CardType;
import com.MindHub.HomeBanking.models.Card;
import com.MindHub.HomeBanking.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
public class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;

    @Test
    public void existCard() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    @Test
    public void existTypeCardCREDIT() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));
    }
}
