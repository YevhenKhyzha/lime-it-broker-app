package com.lime.it.lime.it.broker.api.timer;

import javax.validation.constraints.NotBlank;

public record RequestTimerMessageDetails(
        @NotBlank String message) {
}
