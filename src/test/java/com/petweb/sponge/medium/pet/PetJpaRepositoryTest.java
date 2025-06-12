package com.petweb.sponge.medium.pet;

import com.petweb.sponge.pet.repository.PetEntity;
import com.petweb.sponge.pet.repository.PetJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/pet-repository-test-data.sql")
public class PetJpaRepositoryTest {

    @Autowired
    private PetJpaRepository petJpaRepository;


    @Test
    public void findAllByUserId는_userId로_유저에속한_펫을_모두_찾는다() {
        // given
        Long userId = 1L;

        // when
        List<PetEntity> result = petJpaRepository.findAllByUserId(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("바둑이");
    }
}
