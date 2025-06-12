package com.petweb.sponge.trainer.domain;

import com.petweb.sponge.trainer.dto.HistoryCreate;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class History {

    private Long id;
    private String title;
    private String startDt;
    private String endDt;
    private String description;

    @Builder
    public History(Long id, String title, String startDt, String endDt, String description) {
        this.id = id;
        this.title = title;
        this.startDt = startDt;
        this.endDt = endDt;
        this.description = description;
    }

    public static History from(HistoryCreate historyCreate) {
        return History.builder()
                .title(historyCreate.getTitle())
                .startDt(historyCreate.getStartDt())
                .endDt(historyCreate.getEndDt())
                .description(historyCreate.getDescription())
                .build();
    }
}
