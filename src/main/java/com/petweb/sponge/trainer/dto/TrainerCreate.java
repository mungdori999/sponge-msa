package com.petweb.sponge.trainer.dto;

import com.petweb.sponge.trainer.domain.TrainerAddress;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrainerCreate {

    private String email; //로그인 아이디
    private String name; //이름
    private int gender; //성별
    private String phone; //핸드폰 번호
    private String profileImgUrl; //프로필 이미지 링크
    private String content; //자기소개
    private int years; //연차
    private List<TrainerAddressCreate> trainerAddressList;
    private List<HistoryCreate> historyList;
}
