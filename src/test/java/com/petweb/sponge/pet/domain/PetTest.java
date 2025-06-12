package com.petweb.sponge.pet.domain;

import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.pet.dto.PetCreate;
import com.petweb.sponge.pet.dto.PetUpdate;
import com.petweb.sponge.utils.Gender;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PetTest {


    @Test
    public void petCreate로_PET을_생성할_수_있다() {
        // given
        PetCreate petCreate = PetCreate.builder()
                .name("테스트 이름")
                .breed("테스트 견종")
                .gender(Gender.MALE.getCode())
                .age(5)
                .weight(10.5f)
                .petImgUrl("")
                .build();

        // when
        Pet pet = Pet.from(1L, petCreate,new TestClockHolder(12345L));

        // then
        assertThat(pet.getName()).isEqualTo("테스트 이름");
        assertThat(pet.getBreed()).isEqualTo("테스트 견종");
        assertThat(pet.getGender()).isEqualTo(Gender.MALE.getCode());
        assertThat(pet.getAge()).isEqualTo(5);
        assertThat(pet.getWeight()).isEqualTo(10.5f);
        assertThat(pet.getCreatedAt()).isEqualTo(12345L);

    }

    @Test
    public void petUpdate로_PET_을_수정할_수_있다() {

        // given
        Pet pet = Pet.builder()
                .name("테스트 이름")
                .gender(Gender.MALE.getCode())
                .breed("테스트 견종")
                .age(5)
                .userId(1L)
                .build();

        PetUpdate petUpdate = PetUpdate.builder()
                .name("수정 이름")
                .breed("수정 견종")
                .gender(Gender.NEUTERED_MALE.getCode())
                .age(15)
                .weight(20.4f)
                .petImgUrl("").build();

        // when
        pet = pet.update(petUpdate);

        // then
        assertThat(pet.getName()).isEqualTo("수정 이름");
        assertThat(pet.getBreed()).isEqualTo("수정 견종");
        assertThat(pet.getGender()).isEqualTo(Gender.NEUTERED_MALE.getCode());
        assertThat(pet.getAge()).isEqualTo(15);
        assertThat(pet.getWeight()).isEqualTo(20.4f);
        assertThat(pet.getPetImgUrl()).isEqualTo("");
        assertThat(pet.getUserId()).isEqualTo(1L);

    }

    @Test
    public void 로그인아이디로_로그인을_유저가_했는지_판단할_수_있다() {
        // given
        Pet pet = Pet.builder()
                .name("테스트 이름")
                .gender(Gender.MALE.getCode())
                .breed("테스트 견종")
                .age(5)
                .userId(1L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> pet.checkUser(2L))
                .isInstanceOf(LoginIdError.class);

        assertThatCode(() -> pet.checkUser(1L))
                .doesNotThrowAnyException();

    }

}
