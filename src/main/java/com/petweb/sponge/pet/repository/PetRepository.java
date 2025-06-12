package com.petweb.sponge.pet.repository;

import com.petweb.sponge.pet.domain.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {


    Optional<Pet> findById(Long id);
    List<Pet> findAllByUserId(Long loginId);

    Pet save(Pet pet);

    void delete(Pet pet);
}
