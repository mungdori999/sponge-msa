package com.petweb.sponge.trainer.controller.response;

import com.petweb.sponge.trainer.domain.Trainer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TrainerResponse {

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
    private List<TrainerAddressResponse> trainerAddressList;
    private List<HistoryResponse> historyList;

    public static TrainerResponse from(Trainer trainer) {
        return TrainerResponse.builder()
                .id(trainer.getId())
                .email(trainer.getEmail())
                .name(trainer.getName())
                .gender(trainer.getGender())
                .phone(trainer.getPhone())
                .profileImgUrl(trainer.getProfileImgUrl())
                .content(trainer.getContent())
                .years(trainer.getYears())
                .adoptCount(trainer.getAdoptCount())
                .score(trainer.getScore())
                .chatCount(trainer.getChatCount())
                .trainerAddressList(trainer.getTrainerAddressList().stream().map(TrainerAddressResponse::from).collect(Collectors.toList()))
                .historyList(trainer.getHistoryList().stream().map(HistoryResponse::from).collect(Collectors.toList()))
                .build();
    }
}
