package com.petweb.sponge.trainer.controller.response;

import com.petweb.sponge.trainer.domain.History;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class HistoryResponse {

    private Long id;

    private String title;
    private String startDt;
    private String endDt;
    private String description;
    public static HistoryResponse from(History history) {
        return HistoryResponse.builder()
                .id(history.getId())
                .title(history.getTitle())
                .startDt(history.getStartDt())
                .endDt(history.getEndDt())
                .description(history.getDescription())
                .build();
    }
}
