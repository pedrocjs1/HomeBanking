package com.MindHub.HomeBanking;

import com.MindHub.HomeBanking.Utils.utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UtilsTest {

    @Test
    public void cvvNumberLengthTest(){
        int cvv = utils.randomNumber(100, 999);
        assertThat(String.valueOf(cvv), hasLength(3));
    }
}
