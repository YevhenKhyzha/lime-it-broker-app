package com.lime.it.lime.it.broker.api.timer;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public record CreateTimerRequest(
        @NotBlank String timerName,
        @NotBlank String timerGroup,
        @NotBlank String timerUUId,
        @NotNull ZonedDateTime timerStartDateTime,
        @Valid @NotNull RequestTimerMessageDetails requestTimerMessageDetails) {
}
