package com.petweb.sponge.pet.repository;

import com.petweb.sponge.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepository {
    private final PetJpaRepository petJpaRepository;

    @Override
    public Optional<Pet> findById(Long id) {
        return petJpaRepository.findById(id).map(PetEntity::toModel);
    }

    @Override
    public List<Pet> findAllByUserId(Long loginId) {
        return petJpaRepository.findAllByUserId(loginId).stream().map(PetEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Pet save(Pet pet) {

        return petJpaRepository.save(PetEntity.from(pet)).toModel();
    }

    @Override
    public void delete(Pet pet) {
        petJpaRepository.delete(PetEntity.from(pet));
    }
}
