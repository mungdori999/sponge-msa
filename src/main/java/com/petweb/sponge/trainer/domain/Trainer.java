package com.petweb.sponge.trainer.domain;

import com.petweb.sponge.trainer.dto.TrainerCreate;
import com.petweb.sponge.trainer.dto.TrainerUpdate;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Trainer {

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
    private List<TrainerAddress> trainerAddressList;
    private List<History> historyList;

    @Builder
    public Trainer(Long id, String email, String name, int gender, String phone, String profileImgUrl, String content, int years, int adoptCount, float score, int chatCount, Long createdAt, List<TrainerAddress> trainerAddressList, List<History> historyList) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.profileImgUrl = profileImgUrl;
        this.content = content;
        this.years = years;
        this.adoptCount = adoptCount;
        this.score = score;
        this.chatCount = chatCount;
        this.createdAt = createdAt;
        this.trainerAddressList = trainerAddressList;
        this.historyList = historyList;
    }

    public static Trainer from(TrainerCreate trainerCreate, ClockHolder clockHolder) {
        return Trainer.builder()
                .email(trainerCreate.getEmail())
                .name(trainerCreate.getName())
                .gender(trainerCreate.getGender())
                .phone(trainerCreate.getPhone())
                .profileImgUrl(trainerCreate.getProfileImgUrl())
                .years(trainerCreate.getYears())
                .content(trainerCreate.getContent())
                .createdAt(clockHolder.clock())
                .trainerAddressList(trainerCreate.getTrainerAddressList().stream()
                        .map(TrainerAddress::from).collect(Collectors.toList()))
                .historyList(trainerCreate.getHistoryList().stream()
                        .map(History::from).collect(Collectors.toList()))
                .build();

    }

    public Trainer update(TrainerUpdate trainerUpdate) {
        return Trainer.builder()
                .id(id)
                .email(email)
                .name(trainerUpdate.getName())
                .gender(trainerUpdate.getGender())
                .phone(trainerUpdate.getPhone())
                .profileImgUrl(trainerUpdate.getProfileImgUrl())
                .years(trainerUpdate.getYears())
                .content(trainerUpdate.getContent())
                .chatCount(chatCount)
                .score(score)
                .adoptCount(adoptCount)
                .createdAt(createdAt)
                .trainerAddressList(trainerUpdate.getTrainerAddressList().stream()
                        .map((trainerAddress) -> TrainerAddress.builder().city(trainerAddress.getCity()).town(trainerAddress.getTown()).build()).collect(Collectors.toList()))
                .historyList(trainerUpdate.getHistoryList().stream()
                        .map((history) -> History.builder().title(history.getTitle()).startDt(history.getStartDt()).endDt(history.getEndDt())
                                .description(history.getDescription()).build()).collect(Collectors.toList()))
                .build();
    }

    public void increaseAdoptCount() {
        adoptCount++;
    }

    public void decreaseAdoptCount() {
        if (adoptCount <= 0) {
            throw new IllegalStateException();
        } else {
            adoptCount--;
        }
    }

    public void calcReview(int newScore) {
        if (this.score == 0) {
            // 기존 점수가 없을 경우 새로운 점수 그대로 반영
            this.score = newScore;
        } else {
            // 기존 점수가 있을 경우 평균 계산
            this.score = (this.score + (float) newScore) / 2;
        }
    }
    public void deleteImgUrl() {
        this.profileImgUrl = "";
    }

}
