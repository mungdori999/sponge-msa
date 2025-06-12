package com.petweb.sponge.trainer.domain;

import com.petweb.sponge.trainer.dto.HistoryCreate;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class HistoryTest {

    @Test
    public void trainerCreate로_HISTORY를_생성할_수_있다() {

        // given
        HistoryCreate historyCreate = HistoryCreate.builder()
                .title("헬스 트레이너")
                .startDt("201901")
                .endDt("202312")
                .description("헬스 트레이너로 5년간 활동")
                .build();

        // when
        History history = History.from(historyCreate);

        // then

        // 경력 리스트 검증
        assertThat(history.getTitle()).isEqualTo("헬스 트레이너");
        assertThat(history.getStartDt()).isEqualTo("201901");
        assertThat(history.getEndDt()).isEqualTo("202312");
        assertThat(history.getDescription()).isEqualTo("헬스 트레이너로 5년간 활동");
    }

}
