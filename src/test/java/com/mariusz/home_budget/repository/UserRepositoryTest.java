package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.AppUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
//@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")

public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;



    @Test
    public void findByName() {

        //given
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setPassword("secret");
        user.setEnabled(true);


        testEntityManager.persist(user);
        testEntityManager.flush();

        //when
        AppUser found = userRepository.findByName(user.getName());

        //then
        assertThat(found.getName()).isEqualTo(user.getName());

    }
}