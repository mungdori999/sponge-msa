package com.petweb.sponge.utils;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZoneId;


@Component
public class SystemClockHolder implements ClockHolder {
    @Override
    public long clock() {
        Clock kstClock = Clock.system(ZoneId.of("Asia/Seoul"));
        return kstClock.millis();
    }
}
