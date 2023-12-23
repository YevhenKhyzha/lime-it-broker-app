package com.lime.it.lime.it.broker.timer.model;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record TimerInfo(
        String timerName,
        String timerGroup,
        ZonedDateTime timerTriggeredTime,
        TimerMessageDetails timerMessageDetails) {
}
