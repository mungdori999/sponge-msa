package com.petweb.sponge.trainer.controller.response;

import com.petweb.sponge.trainer.domain.TrainerAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrainerAddressResponse {

    private Long id;
    private String city;
    private String town;
    public static TrainerAddressResponse from(TrainerAddress trainerAddress) {
        return TrainerAddressResponse.builder()
                .id(trainerAddress.getId())
                .city(trainerAddress.getCity())
                .town(trainerAddress.getTown())
                .build();
    }
}
