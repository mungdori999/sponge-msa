package com.petweb.sponge.post.controller.response.answer;

import com.petweb.sponge.trainer.domain.Trainer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrainerShortResponse {

    private Long id;
    private String name; //이름
    private String profileImgUrl; //프로필 이미지 링크
    private int adoptCount; // 채택 답변 수
    private int chatCount; // 1대1 채팅 수

    public static TrainerShortResponse from(Trainer trainer) {
        return TrainerShortResponse.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .profileImgUrl(trainer.getProfileImgUrl())
                .adoptCount(trainer.getAdoptCount())
                .chatCount(trainer.getChatCount())
                .build();
    }
}
