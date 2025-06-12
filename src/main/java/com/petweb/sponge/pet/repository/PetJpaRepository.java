package com.petweb.sponge.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetJpaRepository extends JpaRepository<PetEntity, Long> {

    @Query("SELECT pet FROM PetEntity pet WHERE pet.userId = :loginId")
    List<PetEntity> findAllByUserId(@Param("loginId") Long loginId);

}
