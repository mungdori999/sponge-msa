package com.petweb.sponge.trainer.repository;

import com.petweb.sponge.trainer.domain.History;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.domain.TrainerAddress;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "trainers")
public class TrainerEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String email; //로그인 아이디
    private String name; //이름
    private int gender; //성별
    private String phone; //핸드폰 번호
    private String profileImgUrl; //프로필 이미지 링크
    private String content; //자기소개
    private int years; //연차

    private int adoptCount; // 채택 답변 수
    private float score;
    private int chatCount; // 1대1 채팅 수
    private Long createdAt;

    @OneToMany(mappedBy = "trainerEntity", cascade = CascadeType.ALL)
    private List<TrainerAddressEntity> trainerAddressEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "trainerEntity", cascade = CascadeType.ALL)
    private List<HistoryEntity> historyEntityList = new ArrayList<>();

    @Builder
    public TrainerEntity(String email, String name) {
        this.email = email;
        this.name = name;
    }


    public void addTrainerAddressList(List<TrainerAddressEntity> trainerAddressEntityList) {
        this.trainerAddressEntityList = trainerAddressEntityList;
    }
    public void addHistoryList(List<HistoryEntity> historyEntityList) {
        this.historyEntityList = historyEntityList;
    }

    public Trainer toModel() {

        List<TrainerAddress> trainerAddressList = (!Hibernate.isInitialized(trainerAddressEntityList) || trainerAddressEntityList == null || trainerAddressEntityList.isEmpty())
                ? Collections.emptyList()
                : trainerAddressEntityList.stream()
                .map(TrainerAddressEntity::toModel)
                .collect(Collectors.toList());
        List<History> historyList = (!Hibernate.isInitialized(historyEntityList) || historyEntityList == null || historyEntityList.isEmpty())
                ? Collections.emptyList()
                : historyEntityList.stream()
                .map(HistoryEntity::toModel)
                .collect(Collectors.toList());

        return Trainer.builder()
                .id(id)
                .email(email)
                .name(name)
                .gender(gender)
                .phone(phone)
                .profileImgUrl(profileImgUrl)
                .content(content)
                .years(years)
                .adoptCount(adoptCount)
                .score(score)
                .chatCount(chatCount)
                .createdAt(createdAt)
                .trainerAddressList(trainerAddressList)
                .historyList(historyList)
                .build();

    }

    public static TrainerEntity from(Trainer trainer) {
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.id = trainer.getId();
        trainerEntity.email = trainer.getEmail();
        trainerEntity.name = trainer.getName();
        trainerEntity.gender = trainer.getGender();
        trainerEntity.phone = trainer.getPhone();
        trainerEntity.profileImgUrl = trainer.getProfileImgUrl();
        trainerEntity.content = trainer.getContent();
        trainerEntity.years = trainer.getYears();
        trainerEntity.adoptCount = trainer.getAdoptCount();
        trainerEntity.score = trainer.getScore();
        trainerEntity.chatCount = trainer.getChatCount();
        trainerEntity.createdAt = trainer.getCreatedAt();

        trainerEntity.trainerAddressEntityList = trainer.getTrainerAddressList().stream()
                .map(trainerAddress -> TrainerAddressEntity.builder()
                        .id(trainerAddress.getId())
                        .city(trainerAddress.getCity())
                        .town(trainerAddress.getTown())
                        .trainerEntity(trainerEntity)
                        .build())
                .collect(Collectors.toList());

        trainerEntity.historyEntityList = trainer.getHistoryList().stream()
                .map(history -> HistoryEntity.builder()
                        .id(history.getId())
                        .title(history.getTitle())
                        .startDt(history.getStartDt())
                        .endDt(history.getEndDt())
                        .description(history.getDescription())
                        .trainerEntity(trainerEntity)
                        .build()).collect(Collectors.toList());
        return trainerEntity;
    }


}
