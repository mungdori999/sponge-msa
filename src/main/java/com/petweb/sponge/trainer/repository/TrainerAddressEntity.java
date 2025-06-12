package com.petweb.sponge.trainer.repository;

import com.petweb.sponge.trainer.domain.TrainerAddress;
import com.petweb.sponge.trainer.repository.TrainerEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "trainer_address")
public class TrainerAddressEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String city;
    private String town;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainerEntity;

    @Builder
    public TrainerAddressEntity(Long id, String city, String town, TrainerEntity trainerEntity) {
        this.id = id;
        this.city = city;
        this.town = town;
        this.trainerEntity = trainerEntity;
    }


    public TrainerAddress toModel() {
        return TrainerAddress.builder()
                .id(id)
                .city(city)
                .town(town)
                .build();
    }
}
