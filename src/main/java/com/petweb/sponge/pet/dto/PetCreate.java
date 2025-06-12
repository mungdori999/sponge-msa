package com.petweb.sponge.pet.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetCreate {
    private String name;
    private String breed;
    private int gender;
    private int age;
    private float weight;
    private String petImgUrl;

}
