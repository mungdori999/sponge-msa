package com.petweb.sponge.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder{
    private final long miles;

    @Override
    public long clock() {
        return miles;
    }
}
