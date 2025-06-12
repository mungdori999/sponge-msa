package com.petweb.sponge.trainer.repository;

import com.petweb.sponge.trainer.domain.History;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "history")
public class HistoryEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String startDt;
    private String endDt;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainerEntity;

    public History toModel() {
        return History.builder()
                .id(id)
                .title(title)
                .startDt(startDt)
                .endDt(endDt)
                .description(description)
                .build();
    }

    @Builder
    public HistoryEntity(Long id, String title, String startDt, String endDt, String description, TrainerEntity trainerEntity) {
        this.id = id;
        this.title = title;
        this.startDt = startDt;
        this.endDt = endDt;
        this.description = description;
        this.trainerEntity = trainerEntity;
    }
}
