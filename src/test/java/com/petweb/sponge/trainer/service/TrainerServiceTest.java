package com.petweb.sponge.trainer.service;


import com.petweb.sponge.trainer.domain.History;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.domain.TrainerAddress;
import com.petweb.sponge.trainer.dto.*;
import com.petweb.sponge.trainer.mock.MockTrainerRepository;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.utils.Gender;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainerServiceTest {

    private TrainerService trainerService;

    @BeforeEach()
    void init() {
        TrainerRepository trainerRepository = new MockTrainerRepository();
        trainerService = TrainerService.builder().trainerRepository(trainerRepository).clockHolder(new TestClockHolder(12345L)).build();
        trainerRepository.save(Trainer.builder()
                .email("test1@naver.com")
                .name("test1")
                .gender(Gender.MALE.getCode())
                .profileImgUrl("")
                .content("안녕")
                .years(2)
                .createdAt(0L)
                .trainerAddressList(List.of(
                        TrainerAddress.builder()
                                .city("서울")
                                .town("강남구")
                                .build(),
                        TrainerAddress.builder()
                                .city("부산")
                                .town("해운대구")
                                .build(),
                        TrainerAddress.builder()
                                .city("대구")
                                .town("수성구")
                                .build()
                ))
                .historyList(List.of(History.builder()
                                .title("프로젝트 A")
                                .startDt("202401")
                                .endDt("202406")
                                .description("Spring Boot 기반 웹 애플리케이션 개발")
                                .build(),
                        History.builder()
                                .title("프로젝트 B")
                                .startDt("202307")
                                .endDt("202312")
                                .description("Flutter 기반 모바일 앱 개발")
                                .build(),
                        History.builder()
                                .title("프로젝트 C")
                                .startDt("202203")
                                .endDt("202209")
                                .description("AI 챗봇 서비스 개발")
                                .build()))
                .build());


    }

    @Test
    public void getById는_가입_TRAINER의_정보를_가져온다() {
        // given
        Long id = 1L;

        // when
        Trainer result = trainerService.getById(id);

        // then
        assertThat(result.getEmail()).isEqualTo("test1@naver.com");
        assertThat(result.getTrainerAddressList().size()).isEqualTo(3);
    }

    @Test
    public void create는_TRAINER의_정보를_저장한다() {
        // given
        TrainerCreate trainerCreate = TrainerCreate.builder()
                .email("test@example.com")
                .name("김철수")
                .gender(Gender.MALE.getCode())  // 1: 남성, 2: 여성
                .phone("01012345678")
                .profileImgUrl("")
                .content("안녕하세요! 전문 트레이너입니다.")
                .years(5) // 연차
                .trainerAddressList(List.of(
                        TrainerAddressCreate.builder()
                                .city("서울")
                                .town("강남구")
                                .build(),
                        TrainerAddressCreate.builder()
                                .city("부산")
                                .town("해운대구")
                                .build()
                ))
                .historyList(List.of(
                        HistoryCreate.builder()
                                .title("헬스 트레이너")
                                .startDt("201901")
                                .endDt("202312")
                                .description("헬스 트레이너로 5년간 활동")
                                .build(),
                        HistoryCreate.builder()
                                .title("PT 강사")
                                .startDt("202105")
                                .endDt("202312")
                                .description("개인 PT 강사로 활동")
                                .build()
                ))
                .build();

        // when
        Trainer result = trainerService.create(trainerCreate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("김철수");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getCreatedAt()).isEqualTo(12345L);
        assertThat(result.getTrainerAddressList()).isNotNull();
        assertThat(result.getTrainerAddressList().size()).isEqualTo(2);

        // 주소 검증
        assertThat(result.getTrainerAddressList()).extracting("city")
                .containsExactlyInAnyOrder("서울", "부산");
        assertThat(result.getTrainerAddressList()).extracting("town")
                .containsExactlyInAnyOrder("강남구", "해운대구");

        // 경력 검증
        assertThat(result.getHistoryList()).isNotNull();
        assertThat(result.getHistoryList().size()).isEqualTo(2);
        assertThat(result.getHistoryList()).extracting("title")
                .containsExactlyInAnyOrder("헬스 트레이너", "PT 강사");
    }

    @Test
    public void update는_TRAINER의_정보를_수정한다() {
        // given
        TrainerUpdate trainerUpdate = TrainerUpdate.builder()
                .name("김철수")
                .gender(Gender.MALE.getCode())  // 1: 남성, 2: 여성
                .phone("01012345678")
                .profileImgUrl("")
                .content("안녕하세요! 훈련사입니다")
                .years(3) // 연차
                .trainerAddressList(List.of(
                        TrainerAddressUpdate.builder()
                                .city("서울")
                                .town("도봉산")
                                .build()
                ))
                .historyList(List.of(
                        HistoryUpdate.builder()
                                .title("훈련사")
                                .startDt("201901")
                                .endDt("202312")
                                .description("헬스 트레이너로 5년간 활동")
                                .build()
                ))
                .build();

        // when
        trainerService.update(1L, trainerUpdate);

        // then
        Trainer result = trainerService.getById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("김철수");
        assertThat(result.getGender()).isEqualTo(Gender.MALE.getCode());
        assertThat(result.getPhone()).isEqualTo("01012345678");
        assertThat(result.getProfileImgUrl()).isEqualTo("");
        assertThat(result.getContent()).isEqualTo("안녕하세요! 훈련사입니다");
        assertThat(result.getYears()).isEqualTo(3);

        // 주소 리스트 검증
        assertThat(result.getTrainerAddressList()).isNotNull();
        assertThat(result.getTrainerAddressList().size()).isEqualTo(1);
        assertThat(result.getTrainerAddressList().get(0).getCity()).isEqualTo("서울");
        assertThat(result.getTrainerAddressList().get(0).getTown()).isEqualTo("도봉산");

        // 경력 리스트 검증
        assertThat(result.getHistoryList()).isNotNull();
        assertThat(result.getHistoryList().size()).isEqualTo(1);
        assertThat(result.getHistoryList().get(0).getTitle()).isEqualTo("훈련사");
        assertThat(result.getHistoryList().get(0).getStartDt()).isEqualTo("201901");
        assertThat(result.getHistoryList().get(0).getEndDt()).isEqualTo("202312");
        assertThat(result.getHistoryList().get(0).getDescription()).isEqualTo("헬스 트레이너로 5년간 활동");


    }




}
