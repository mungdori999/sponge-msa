package com.petweb.sponge.trainer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryCreate {
    private String title;
    private String startDt;
    private String endDt;
    private String description;
}
