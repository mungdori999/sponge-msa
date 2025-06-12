package com.petweb.sponge.trainer.domain;

import com.petweb.sponge.trainer.dto.TrainerAddressCreate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainerAddressTest {


    @Test
    public void trainerAddressCreate로_TRAINERADDRESS를_생성할_수_있다() {

        // given
        TrainerAddressCreate trainerAddressCreate = TrainerAddressCreate.builder()
                .city("서울")
                .town("강남구")
                .build();

        // when
        TrainerAddress trainerAddress = TrainerAddress.from(trainerAddressCreate);

        // then

        // 경력 리스트 검증
        assertThat(trainerAddress.getCity()).isEqualTo("서울");
        assertThat(trainerAddress.getTown()).isEqualTo("강남구");
    }
}
