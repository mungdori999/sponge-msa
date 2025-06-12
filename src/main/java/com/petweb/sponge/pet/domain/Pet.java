package com.petweb.sponge.pet.domain;


import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.pet.dto.PetCreate;
import com.petweb.sponge.pet.dto.PetUpdate;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Pet {

    private Long id;
    private String name;
    private String breed;
    private int gender;
    private int age;
    private float weight;
    private String petImgUrl;
    private Long userId;
    private Long createdAt;

    @Builder
    public Pet(Long id, String name, String breed, int gender, int age, float weight, String petImgUrl, Long userId, Long createdAt) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.petImgUrl = petImgUrl;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public static Pet from(Long userId, PetCreate petCreate, ClockHolder clockHolder) {
        return Pet.builder()
                .name(petCreate.getName())
                .breed(petCreate.getBreed())
                .gender(petCreate.getGender())
                .age(petCreate.getAge())
                .weight(petCreate.getWeight())
                .petImgUrl(petCreate.getPetImgUrl())
                .userId(userId)
                .createdAt(clockHolder.clock())
                .build();
    }

    public Pet update(PetUpdate petUpdate) {

        return Pet.builder()
                .id(id)
                .name(petUpdate.getName())
                .breed(petUpdate.getBreed())
                .gender(petUpdate.getGender())
                .age(petUpdate.getAge())
                .weight(petUpdate.getWeight())
                .petImgUrl(petUpdate.getPetImgUrl())
                .userId(userId)
                .createdAt(createdAt)
                .build();
    }

    public void checkUser(Long loginId) {
        if (!userId.equals(loginId)) {
            throw new LoginIdError();
        }
    }
}
