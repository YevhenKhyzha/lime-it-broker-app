package com.lime.it.lime.it.broker.api.timer;

import java.time.ZonedDateTime;

public record TimerInfoResponse(
        String timerName,
        String timerGroup,
        ZonedDateTime timerTriggeredTime,
        TimerInfoMessageDetails timerInfoMessageDetails) {
}
