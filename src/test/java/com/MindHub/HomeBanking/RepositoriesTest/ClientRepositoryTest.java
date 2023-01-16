package com.MindHub.HomeBanking.RepositoriesTest;


import com.MindHub.HomeBanking.models.Client;
import com.MindHub.HomeBanking.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;


    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));

    }

}
