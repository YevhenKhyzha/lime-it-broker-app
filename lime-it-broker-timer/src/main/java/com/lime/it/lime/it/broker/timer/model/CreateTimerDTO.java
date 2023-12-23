package com.lime.it.lime.it.broker.timer.model;

import java.time.ZonedDateTime;

public record CreateTimerDTO(
        String timerName,
        String timerGroup,
        String timerUUId,
        ZonedDateTime timerStartDateTime,
        CreateTimerMessageDetailsDTO requestTimerMessageDetails) {
}
