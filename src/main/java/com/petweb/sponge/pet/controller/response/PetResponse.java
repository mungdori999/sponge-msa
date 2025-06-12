package com.petweb.sponge.pet.controller.response;

import com.petweb.sponge.pet.domain.Pet;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetResponse {

    private Long id;
    private String name;
    private String breed;
    private int gender;
    private int age;
    private float weight;
    private String petImgUrl;

    public static PetResponse from(Pet pet) {

        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .age(pet.getAge())
                .weight(pet.getWeight())
                .petImgUrl(pet.getPetImgUrl())
                .build();
    }
}
